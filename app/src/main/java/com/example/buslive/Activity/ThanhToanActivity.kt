package com.example.buslive.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.example.buslive.R

class ThanhToanActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseKhachHang: DatabaseReference
    private lateinit var databaseVeXe: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thanh_toan)

        // Khởi tạo Firebase
        auth = FirebaseAuth.getInstance()
        databaseKhachHang = FirebaseDatabase.getInstance().getReference("KhachHang")
        databaseVeXe = FirebaseDatabase.getInstance().getReference("VeXe")

        // Nhận dữ liệu từ Intent
        val tenNhaXe = intent.getStringExtra("tenNhaXe") ?: ""
        val gioDi = intent.getStringExtra("gioDi") ?: ""
        val ngayKhoiHanh = intent.getStringExtra("ngayKhoiHanh") ?: ""
        val viTri = intent.getStringExtra("viTri") ?: ""
        val gia = intent.getIntExtra("gia", 0)
        val tang = intent.getIntExtra("tang", 1)
        val maChuyen = intent.getStringExtra("maChuyen") ?: ""

        // Ánh xạ view
        val txtTenTuyen = findViewById<TextView>(R.id.txtTenTuyen)
        val txtNgayGio = findViewById<TextView>(R.id.txtNgayGio)
        val txtTenKhach = findViewById<TextView>(R.id.txtTenKhach)
        val txtSdtKhach = findViewById<TextView>(R.id.txtSdtKhach)
        val txtEmailKhach = findViewById<TextView>(R.id.txtEmailKhach)
        val btnThanhToan = findViewById<Button>(R.id.btnThanhToan)
        val paymentMethodGroup = findViewById<RadioGroup>(R.id.paymentMethodGroup)
        val backButton = findViewById<ImageView>(R.id.backButton)

        // Gán thông tin chuyến đi
        txtTenTuyen.text = "Nhà xe: $tenNhaXe - Mã chuyến: $maChuyen"
        txtNgayGio.text = "Khởi hành: $ngayKhoiHanh lúc $gioDi"

        // Gán thông tin khách hàng
        val uid = auth.currentUser?.uid
        if (uid != null) {
            databaseKhachHang.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val ten = snapshot.child("tenKhachHang").getValue(String::class.java) ?: ""
                    val sdt = snapshot.child("soDienThoai").getValue(String::class.java) ?: ""
                    val email = snapshot.child("email").getValue(String::class.java) ?: ""

                    txtTenKhach.text = "Tên: $ten"
                    txtSdtKhach.text = "SĐT: $sdt"
                    txtEmailKhach.text = "Email: $email"
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ThanhToanActivity, "Lỗi tải thông tin khách hàng", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // Xử lý quay lại
        backButton.setOnClickListener {
            finish()
        }

        // Xử lý thanh toán
        btnThanhToan.setOnClickListener {
            val selectedId = paymentMethodGroup.checkedRadioButtonId
            if (selectedId == -1) {
                Toast.makeText(this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val phuongThuc = findViewById<RadioButton>(selectedId).text.toString()

            if (uid != null) {
                val veID = databaseVeXe.child(uid).push().key ?: return@setOnClickListener
                val ve = hashMapOf(
                    "maChuyen" to maChuyen,
                    "tenNhaXe" to tenNhaXe,
                    "ngayKhoiHanh" to ngayKhoiHanh,
                    "gioDi" to gioDi,
                    "viTri" to viTri,
                    "gia" to gia,
                    "tang" to tang,
                    "phuongThuc" to phuongThuc
                )

                databaseVeXe.child(uid).child(veID).setValue(ve).addOnSuccessListener {
                    Toast.makeText(this, "Đặt vé thành công với $phuongThuc", Toast.LENGTH_SHORT).show()
                    // Chuyển sang màn hình xác nhận (tuỳ chọn)
                    // startActivity(Intent(this, XacNhanActivity::class.java))
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "Lỗi khi lưu vé", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
