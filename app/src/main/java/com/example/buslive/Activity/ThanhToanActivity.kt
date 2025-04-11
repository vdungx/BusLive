package com.example.buslive.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.buslive.Model.Cabin
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
        databaseKhachHang = dbRef.getReference("users")
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
                txtTenKhach.text = "Tên: ${snapshot.child("fullName").getValue(String::class.java) ?: ""}"
                txtSdtKhach.text = "SĐT: ${snapshot.child("phone").getValue(String::class.java) ?: ""}"
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
                val ticketId = databaseVeXe.push().key ?: return@setOnClickListener

                // Lấy dữ liệu từ intent
                val currentTime = java.text.SimpleDateFormat("HH:mm:ss dd/MM/yyyy", java.util.Locale.getDefault()).format(java.util.Date())
                val maChuyen = intent.getStringExtra("maChuyen") ?: return@setOnClickListener
                val tenNhaXe = intent.getStringExtra("tenNhaXe") ?: ""
                val gioDi = intent.getStringExtra("gioDi") ?: ""
                val ngayKhoiHanh = intent.getStringExtra("ngayKhoiHanh") ?: ""
                val viTri = intent.getStringExtra("viTri") ?: ""
                val tang = intent.getIntExtra("tang", 1)

                // Truy vấn Firebase để lấy đúng maCabin
                val cabinRef = FirebaseDatabase.getInstance().getReference("Cabin").child(maChuyen)
                cabinRef.orderByChild("viTri").equalTo(viTri)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (child in snapshot.children) {
                                val cabin = child.getValue(Cabin::class.java)
                                if (cabin != null && cabin.tang == tang) {
                                    val maCabin = child.key ?: continue
                                    val ticketData = mapOf(
                                        "maCabin" to maCabin,
                                        "maKH" to uid,
                                        "maChuyen" to maChuyen,
                                        "maThanhToan" to paymentMethod,
                                        "thoiGianDatVe" to currentTime,
                                        "trangThai" to "Đã đặt"
                                    )

                                    FirebaseDatabase.getInstance().getReference("VeXe").child(ticketId)
                                        .setValue(ticketData)
                                        .addOnSuccessListener {
                                            Toast.makeText(this@ThanhToanActivity, "Đặt vé thành công với $paymentMethod", Toast.LENGTH_SHORT).show()
                                            val intent = Intent(this@ThanhToanActivity, MainActivity::class.java).apply {
                                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                putExtra("paymentSuccess", true)
                                            }
                                            startActivity(intent)
                                            finish()
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(this@ThanhToanActivity, "Lỗi khi lưu vé", Toast.LENGTH_SHORT).show()
                                        }

                                    return  // sau khi lưu xong thì không cần xử lý tiếp
                                }
                            }

                            Toast.makeText(this@ThanhToanActivity, "Không tìm thấy cabin phù hợp", Toast.LENGTH_SHORT).show()
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@ThanhToanActivity, "Lỗi Firebase: ${error.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
            } else {
                Toast.makeText(this, "Không tìm thấy người dùng", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
