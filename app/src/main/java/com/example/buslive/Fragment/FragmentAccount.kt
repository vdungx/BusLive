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
import com.example.buslive.Activity.ChangePasswordActivity
import com.example.buslive.Activity.EditProfileActivity
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

        setupLogout(view)
        setupSupport(view)
        setupFeedback(view)
        loadUserInfo(view)
        setupEditProfile(view)
        setupChangePassword(view)
    }

    private fun setupLogout(view: View) {
        val itemLogout = view.findViewById<View>(R.id.itemLogout)
        val tvTitle = itemLogout.findViewById<TextView>(R.id.title)
        val ivIcon = itemLogout.findViewById<ImageView>(R.id.icon)

        tvTitle.text = "Đăng xuất"
        ivIcon.setImageResource(R.drawable.ic_logout)

        itemLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Đăng xuất khỏi tài khoản?")
                .setCancelable(true)
                .setNegativeButton("Hủy", null)
                .setPositiveButton("Xác Nhận") { _, _ ->
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(requireContext(), LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                }
                .show()
        }
    }

    private fun setupSupport(view: View) {
        val itemSupport = view.findViewById<View>(R.id.itemSupportCenter)
        val tvTitle = itemSupport.findViewById<TextView>(R.id.title)
        val ivIcon = itemSupport.findViewById<ImageView>(R.id.icon)

        tvTitle.text = "Trung tâm hỗ trợ"
        ivIcon.setImageResource(R.drawable.ic_support1)

        itemSupport.setOnClickListener {
            startActivity(Intent(requireContext(), SupportActivity::class.java))
        }
    }

    private fun setupFeedback(view: View) {
        val itemFeedback = view.findViewById<View>(R.id.itemFeedback)
        val tvTitle = itemFeedback.findViewById<TextView>(R.id.title)
        val ivIcon = itemFeedback.findViewById<ImageView>(R.id.icon)

        tvTitle.text = "Góp Ý"
        ivIcon.setImageResource(R.drawable.ic_mail)

        itemFeedback.setOnClickListener {
            startActivity(Intent(requireContext(), GopyActivity::class.java))
        }
    }

    private fun loadUserInfo(view: View) {
        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvRole = view.findViewById<TextView>(R.id.tvRole)
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
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
                    // Log or handle error
                }
            })
    }

    private fun setupEditProfile(view: View) {
        val itemEdit = view.findViewById<View>(R.id.itemEditProfile)
        val tvTitle = itemEdit.findViewById<TextView>(R.id.title)
        val ivIcon = itemEdit.findViewById<ImageView>(R.id.icon)

        tvTitle.text = "Chỉnh sửa thông tin"
        ivIcon.setImageResource(R.drawable.ic_edit_profile)

        itemEdit.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }
    }

    private fun setupChangePassword(view: View) {
        val itemPassword = view.findViewById<View>(R.id.itemChangePassword)
        val tvTitle = itemPassword.findViewById<TextView>(R.id.title)
        val ivIcon = itemPassword.findViewById<ImageView>(R.id.icon)

        tvTitle.text = "Đổi mật khẩu"
        ivIcon.setImageResource(R.drawable.ic_password)

        itemPassword.setOnClickListener {
            startActivity(Intent(requireContext(), ChangePasswordActivity::class.java))
        }
    }

    private fun convertRole(role: String): String {
        return when (role.lowercase()) {
            "customer" -> "Thành viên"
            "bus_owner" -> "Nhà xe"
            "admin" -> "Quản trị viên"
            else -> ""
        }
    }
}
