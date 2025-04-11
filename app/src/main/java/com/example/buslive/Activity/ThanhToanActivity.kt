package com.example.buslive.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.buslive.Model.Ticket
import com.example.buslive.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ThanhToanActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseKhachHang: DatabaseReference
    private lateinit var databaseVeXe: DatabaseReference

    // View
    private lateinit var txtTenTuyen: TextView
    private lateinit var txtNgayGio: TextView
    private lateinit var txtTenKhach: TextView
    private lateinit var txtSdtKhach: TextView
    private lateinit var txtEmailKhach: TextView
    private lateinit var btnThanhToan: Button
    private lateinit var paymentMethodGroup: RadioGroup
    private lateinit var backButton: ImageView

    // Dữ liệu nhận từ Intent
    private var tenNhaXe = ""
    private var gioDi = ""
    private var ngayKhoiHanh = ""
    private var viTri = ""
    private var gia = 0
    private var tang = 1
    private var maChuyen = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thanh_toan)

        initFirebase()
        getIntentData()
        setupViews()
        loadUserInfo()
        handleActions()
    }

    private fun initFirebase() {
        auth = FirebaseAuth.getInstance()
        val dbRef = FirebaseDatabase.getInstance()
        databaseKhachHang = dbRef.getReference("KhachHang")
        databaseVeXe = dbRef.getReference("VeXe")
    }

    private fun getIntentData() {
        intent?.apply {
            tenNhaXe = getStringExtra("tenNhaXe") ?: ""
            gioDi = getStringExtra("gioDi") ?: ""
            ngayKhoiHanh = getStringExtra("ngayKhoiHanh") ?: ""
            viTri = getStringExtra("viTri") ?: ""
            gia = getIntExtra("gia", 0)
            tang = getIntExtra("tang", 1)
            maChuyen = getStringExtra("maChuyen") ?: ""
        }
    }

    private fun setupViews() {
        txtTenTuyen = findViewById(R.id.txtTenTuyen)
        txtNgayGio = findViewById(R.id.txtNgayGio)
        txtTenKhach = findViewById(R.id.txtTenKhach)
        txtSdtKhach = findViewById(R.id.txtSdtKhach)
        txtEmailKhach = findViewById(R.id.txtEmailKhach)
        btnThanhToan = findViewById(R.id.btnThanhToan)
        paymentMethodGroup = findViewById(R.id.paymentMethodGroup)
        backButton = findViewById(R.id.backButton)

        txtTenTuyen.text = "Nhà xe: $tenNhaXe - Mã chuyến: $maChuyen"
        txtNgayGio.text = "Khởi hành: $ngayKhoiHanh lúc $gioDi"
    }

    private fun loadUserInfo() {
        val uid = auth.currentUser?.uid ?: return
        databaseKhachHang.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                txtTenKhach.text = "Tên: ${snapshot.child("tenKhachHang").getValue(String::class.java) ?: ""}"
                txtSdtKhach.text = "SĐT: ${snapshot.child("soDienThoai").getValue(String::class.java) ?: ""}"
                txtEmailKhach.text = "Email: ${snapshot.child("email").getValue(String::class.java) ?: ""}"
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ThanhToanActivity, "Lỗi tải thông tin khách hàng", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun handleActions() {
        backButton.setOnClickListener { finish() }

        btnThanhToan.setOnClickListener {
            val selectedId = paymentMethodGroup.checkedRadioButtonId
            if (selectedId == -1) {
                Toast.makeText(this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val paymentMethod = findViewById<RadioButton>(selectedId).text.toString()
            val uid = auth.currentUser?.uid

            if (uid != null) {
                val ticketId = databaseVeXe.child(uid).push().key ?: return@setOnClickListener

                val route = "Mã chuyến: $maChuyen - $tenNhaXe"
                val company = tenNhaXe
                val type = "Tầng $tang - Ghế $viTri"
                val time = "$ngayKhoiHanh lúc $gioDi"
                val bookingTime = paymentMethod

                val ticket = Ticket(
                    route = route,
                    company = company,
                    type = type,
                    time = time,
                    bookingTime = bookingTime
                )

                databaseVeXe.child(uid).child(ticketId).setValue(ticket)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Đặt vé thành công với $paymentMethod", Toast.LENGTH_SHORT).show()

                        // Quay về màn hình chính
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        intent.putExtra("paymentSuccess", true) // nếu bạn muốn hiển thị thông báo ở MainActivity
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Lỗi khi lưu vé", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Không tìm thấy người dùng", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
