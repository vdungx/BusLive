package com.example.buslive.Fragment



import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.buslive.Activity.GopyActivity
import com.example.buslive.Activity.LoginActivity
import com.example.buslive.Activity.SupportActivity
import com.example.buslive.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentAccount : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account_customer, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemLogout = view.findViewById<View>(R.id.itemLogout)
        val tvTitle = itemLogout.findViewById<TextView>(R.id.title)
        val ivIcon = itemLogout.findViewById<ImageView>(R.id.icon)

        tvTitle.text = "Đăng xuất"
        ivIcon.setImageResource(R.drawable.ic_logout) // thay bằng icon bạn muốn
        itemLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Đăng xuất khỏi tài khoản?")
                .setCancelable(true)
                .setNegativeButton("Hủy", null)
                .setPositiveButton("Xác Nhận") { _, _ ->
                    FirebaseAuth.getInstance().signOut()

                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                .show()
        }

        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvRole = view.findViewById<TextView>(R.id.tvRole)

        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid

        if (uid != null) {
            val databaseRef = FirebaseDatabase.getInstance().getReference("users")

            databaseRef.orderByChild("userId").equalTo(uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (userSnapshot in snapshot.children) {
                            val fullName = userSnapshot.child("fullName").getValue(String::class.java) ?: "Không rõ"
                            val role = userSnapshot.child("role").getValue(String::class.java) ?: "Thành viên"

                            tvName.text = fullName
                            tvRole.text = convertRole(role)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Xử lý lỗi nếu cần
                    }
                })
        }
        val itemSupport = view.findViewById<View>(R.id.itemSupportCenter)
        val tvTitleSupport = itemSupport.findViewById<TextView>(R.id.title)
        val ivIconSupport = itemSupport.findViewById<ImageView>(R.id.icon)

        tvTitleSupport.text = "Trung tâm hỗ trợ"
        ivIconSupport.setImageResource(R.drawable.ic_support1) // thay bằng icon bạn muốn

        itemSupport.setOnClickListener {
            val intent = Intent(requireContext(), SupportActivity::class.java)
            startActivity(intent)
        }

        val itemGopY = view.findViewById<View>(R.id.itemFeedback)
        val tvTitleGopY = itemGopY.findViewById<TextView>(R.id.title)
        val ivIconGopY = itemGopY.findViewById<ImageView>(R.id.icon)
        tvTitleGopY.text = "Góp Ý"
        ivIconGopY.setImageResource(R.drawable.ic_mail) // thay bằng icon bạn muốn
        itemGopY.setOnClickListener {
            val intent = Intent(requireContext(), GopyActivity::class.java)
            startActivity(intent)
        }
    }

    private fun convertRole(role: String): String {
        return when(role.lowercase()) {
            "customer" -> "Thành viên"
            "bus_owner" -> "Nhà xe"
            "admin" -> "Quản trị viên"
            else -> ""
        }
    }
}