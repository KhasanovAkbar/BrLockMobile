package mobile.uz.brlock_mobile.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.view.*
import androidx.appcompat.widget.SearchView
import kotlinx.android.synthetic.main.fragment_search.*
import mobile.uz.brlock_mobile.R
import mobile.uz.brlock_mobile.base.BaseFragment
import mobile.uz.brlock_mobile.base.BaseViewModel
import mobile.uz.brlock_mobile.domain.Entity
import mobile.uz.brlock_mobile.domain.search.SearchUser
import mobile.uz.brlock_mobile.ui.activity.MainActivity
import mobile.uz.brlock_mobile.ui.adapters.UsersRecyclerViewAdapter
import mobile.uz.brlock_mobile.utils.extentions.toast
import okhttp3.internal.notify
import okhttp3.internal.notifyAll
import java.util.*
import kotlin.collections.ArrayList

class SearchFragment : BaseFragment(R.layout.fragment_search) {
    override lateinit var mainActivity: MainActivity
    override lateinit var viewModel: BaseViewModel

    private var COD_REQUEST = 1
    private lateinit var photoUri: Uri
    private lateinit var currentPhotoPath: String
    private var imagePath: String? = null
    private var isClick = -1
    private var userName: String? = null
    private var userSurname: String? = null
    private var userList = ArrayList<Entity>()

    private lateinit var adapter: UsersRecyclerViewAdapter

    @SuppressLint("SetTextI18n")
    override fun initialize() {
        mainActivity = (requireActivity() as MainActivity)
        viewModel = mainActivity.viewModel
        showProgress(false)
        adapter = UsersRecyclerViewAdapter()

        if (userName == null && userSurname == null) {
            account_image.text =
                viewModel.fetchUser().userJpo!!.name.substring(0, 1)
                    .uppercase() + viewModel.fetchUser().userJpo!!.surname.substring(0, 1)
                    .uppercase()
        } else {
            account_image.text =
                userName!!.substring(0, 1)
                    .uppercase() + userSurname!!.substring(0, 1)
                    .uppercase()
        }
        account_image.setOnClickListener {
            addFragment(EditUserFragment())
        }

        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query!!.isNotEmpty()) {
                    val uppercase = query.uppercase()
                    viewModel.getByPhoneNumber(uppercase)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

    }

    companion object {
        fun newInstance(name: String, surname: String) = SearchFragment().apply {
            userName = name
            userSurname = surname
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun observe() {
        viewModel.single.observe(
            viewLifecycleOwner,
            {
                showProgress(false)
                if (it is SearchUser) {
                    if (!it.requestFailed) {
                        adapter.setData(it.entities)
                    }
                }
            }
        )
    }
}


/*


  override fun setUserVisibleHint(isVisibleToUser: Boolean) {
      super.setUserVisibleHint(isVisibleToUser)
      if (isVisibleToUser) {
          childFragmentManager
              .beginTransaction()
              .detach(this)
              .attach(this)
              .commit()
      }
  }

  private fun permission() {
      askPermission(
          Manifest.permission.CAMERA,
          Manifest.permission.READ_EXTERNAL_STORAGE,
      ) {
          val bottomSheetDialog = BottomSheetDialog(requireContext())
          val bottomView =
              BottomDialogBinding.inflate(LayoutInflater.from(context), null, false)
          bottomView.camera.setOnClickListener {
              isClick = 1
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                  cameraNew()
              } else {
                  cameraOld()
              }
              bottomSheetDialog.dismiss()
          }
          bottomView.storage.setOnClickListener {
              isClick = 0
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                  pickImageFromNewGallery()
              } else {
                  pickImageFromOldGallery()
              }
              bottomSheetDialog.dismiss()

          }
          bottomSheetDialog.setContentView(bottomView.root)
          bottomSheetDialog.show()
      }.onDeclined { e ->
          if (e.hasDenied()) {
              toast(requireContext(), "Denied")
              AlertDialog.Builder(requireContext())
                  .setMessage("Permission must be granted in order to display contacts information")
                  .setPositiveButton("yes") { dialog, which ->
                      e.askAgain()
                  }
                  .setNegativeButton("no") { dialog, which ->
                      dialog.dismiss()
                  }
                  .show()
          }
          if (e.hasForeverDenied()) {
              toast(requireContext(), "Denied")
              e.goToSettings()
          }
      }
  }

  @Throws(IOException::class)
  private fun createImageFile(): File {
      val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
      val storageDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
      return File.createTempFile(
          "JPEG_${timeStamp}_",
          ".jpg",
          storageDir
      ).apply {
          currentPhotoPath = absolutePath
      }

  }

  override fun onActivityResult(
      requestCode: Int,
      resultCode: Int,
      data: Intent?
  ) {
      super.onActivityResult(requestCode, resultCode, data)
      if (isClick == 0) {
          if (requestCode == COD_REQUEST && resultCode == Activity.RESULT_OK) {
              val uri = data?.data ?: return
              image_view.setImageURI(uri)
              val openInputStream = activity?.contentResolver?.openInputStream(uri)
              val file = File(
                  activity?.filesDir,
                  "${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}.jpg"
              )
              val fileOutputStream = FileOutputStream(file)
              openInputStream?.copyTo(fileOutputStream)
              openInputStream?.close()
              fileOutputStream.close()
              val absolutePath = file.absolutePath
              imagePath = absolutePath
          }
          isClick = -1
      } else if (isClick == 1) {
          if (::currentPhotoPath.isInitialized) {
              image_view.setImageURI(Uri.fromFile(File(currentPhotoPath)))
              val openInputStream =
                  activity?.contentResolver?.openInputStream(
                      Uri.fromFile(
                          File(
                              currentPhotoPath
                          )
                      )
                  )
              val file = File(
                  activity?.filesDir,
                  "${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}.jpg"
              )
              val fileOutputStream = FileOutputStream(file)
              openInputStream?.copyTo(fileOutputStream)
              openInputStream?.close()
              fileOutputStream.close()
              val absolutePath = file.absolutePath
              imagePath = absolutePath
          }
          isClick = -1
      }
  }

  private
  val getTakeImageContent =
      registerForActivityResult(ActivityResultContracts.TakePicture()) {
          if (it) {
              image_view.setImageURI(photoUri)
              val openInputStream =
                  activity?.contentResolver?.openInputStream(photoUri)
              val file = File(
                  activity?.filesDir,
                  "${
                      SimpleDateFormat(
                          "yyyyMMdd_HHmmss",
                          Locale.US
                      ).format(Date())
                  }.jpg"
              )
              val fileOutputStream = FileOutputStream(file)
              openInputStream?.copyTo(fileOutputStream)
              openInputStream?.close()
              fileOutputStream.close()
              val absolutePath = file.absolutePath
              imagePath = absolutePath
          }
      }

  private
  val getImageContent =
      registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
          uri ?: return@registerForActivityResult
          if (uri.equals(null)) {
              image_view.setImageResource(R.drawable.ic_baseline_person_24)
          } else {
              image_view.setImageURI(uri)
          }
          val openInputStream =
              activity?.contentResolver?.openInputStream(uri)
          val file = File(
              activity?.filesDir,
              "${
                  SimpleDateFormat(
                      "yyyyMMdd_HHmmss",
                      Locale.US
                  ).format(Date())
              }.jpg"
          )
          val fileOutputStream = FileOutputStream(file)
          openInputStream?.copyTo(fileOutputStream)
          openInputStream?.close()
          fileOutputStream.close()
          val absolutePath = file.absolutePath
          imagePath = absolutePath

      }

  private fun pickImageFromNewGallery() {
      getImageContent.launch("image/*")
  }

  private fun pickImageFromOldGallery() {
      startActivityForResult(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
          addCategory(Intent.CATEGORY_OPENABLE)
          type = "image/*"
      }, COD_REQUEST)
  }

  private fun cameraNew() {
      getTakeImageContent.launch(photoUri)
  }

  private fun cameraOld() {
      Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
          takePictureIntent.resolveActivity(requireActivity().packageManager)
              ?.also {
                  val photoFile = createImageFile()
                  photoFile.also {
                      val phoneUri =
                          FileProvider.getUriForFile(
                              requireContext(),
                              BuildConfig.APPLICATION_ID,
                              it
                          )
                      takePictureIntent.putExtra(
                          MediaStore.EXTRA_OUTPUT,
                          phoneUri
                      )
                      startActivityForResult(
                          takePictureIntent,
                          COD_REQUEST
                      )
                      isClick = 1
                  }
              }
      }
  }*/