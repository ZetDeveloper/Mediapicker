package com.jiren.mediapicker.fragments

import android.database.ContentObserver
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.jiren.mediapicker.*
import com.jiren.mediapicker.adapters.MediaAdapter
import com.jiren.mediapicker.adapters.OthersFileAdapter
import com.jiren.mediapicker.domain.models.FileGenericModel
import com.jiren.mediapicker.utils.AndroidLifecycleUtils
import com.jiren.mediapicker.viewmodels.MediaOthersViewmodel
import com.jiren.mediapicker.viewmodels.MediaViewModel
import com.jiren.mediapicker.utils.PickerUtils
import com.jiren.mediapicker.utils.QueryFiles
import kotlinx.coroutines.*
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val FILE_TYPE = "FILE_TYPE"


/**
 * A simple [Fragment] subclass.
 * Use the [MediaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MediaFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var fileType: Int? = null
    lateinit var recyclerMaster: RecyclerView
    private lateinit var mGlideRequestManager: RequestManager
    private val SCROLL_THRESHOLD = 30
    lateinit var viewModel: MediaViewModel
    lateinit var viewModelOthers: MediaOthersViewmodel
    private var contentObserver: ContentObserver? = null

    private val viewModelJob = SupervisorJob()
    private val exceptionHandler = CoroutineExceptionHandler { _, t ->
        val err = t
        t.printStackTrace()
    }
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob + exceptionHandler)
    fun launchDataLoad(block: suspend (scope: CoroutineScope) -> Unit): Job {
        return uiScope.launch {
            try {
                block(this)
            } catch (error: Exception) {
                handleException(error)
            } finally {
            }
        }
    }

    private fun handleException(error: Exception) {
        error.printStackTrace()
        if (error !is CancellationException) {

        }
    }

    private fun resumeRequestsIfNotDestroyed() {
        if (!AndroidLifecycleUtils.canLoadImage(this)) {
            return
        }

        mGlideRequestManager.resumeRequests()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(MediaViewModel::class.java)
        viewModelOthers = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(MediaOthersViewmodel::class.java)
        arguments?.let {
            fileType = it.getInt(FILE_TYPE)
        }
    }

    private fun initView(view: View) {
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        mGlideRequestManager = Glide.with(this)
        recyclerMaster = view.findViewById(R.id.recyclerMaster)
        recyclerMaster.itemAnimator = DefaultItemAnimator()
        recyclerMaster.layoutManager =  layoutManager
        recyclerMaster.setHasFixedSize(true)
        recyclerMaster.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Log.d(">>> Picker >>>", "dy = " + dy);
                if (Math.abs(dy) > SCROLL_THRESHOLD) {
                    mGlideRequestManager.pauseRequests()
                } else {
                    resumeRequestsIfNotDestroyed()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    resumeRequestsIfNotDestroyed()
                }
            }
        })

        viewModelOthers.lvFiles.observe(viewLifecycleOwner, Observer { data ->
            if(fileType == PickerConstant.MEDIA_TYPE_OTHERS || fileType == PickerConstant.MEDIA_TYPE_MUSIC)
            {
                adapterOthersDataObserver?.setData(data)
            }

        })

        viewModel.lvFiles.observe(viewLifecycleOwner, Observer { data ->
            if(!(fileType == PickerConstant.MEDIA_TYPE_OTHERS || fileType == PickerConstant.MEDIA_TYPE_MUSIC))
            {
                adapterDataObserver?.setData(data)
            }

        })
        context.let {
            if(fileType == PickerConstant.MEDIA_TYPE_OTHERS || fileType == PickerConstant.MEDIA_TYPE_MUSIC)
            {
               viewModelOthers.getMedia(fileType!!)
                adapterOthersDataObserver = OthersFileAdapter(context!! , mGlideRequestManager,3)
                recyclerMaster.adapter = adapterOthersDataObserver
            }else {
                viewModel.getMedia(fileType!!)
                adapterDataObserver = MediaAdapter(context!!, mGlideRequestManager)
                recyclerMaster.adapter = adapterDataObserver
            }

        }


    }

    private fun registerContentObserver() {
        if (contentObserver == null) {
            contentObserver = context!!.contentResolver.registerObserver(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            ) {

            }
        }
    }
    private var adapterDataObserver: MediaAdapter? = null
    private var adapterOthersDataObserver: OthersFileAdapter? = null


    private fun queryOthers() {
        launchDataLoad {
            val medias = mutableListOf<FileGenericModel>()

            QueryFiles().queryDocs(
                fileTypes = if (fileType == PickerConstant.MEDIA_TYPE_MUSIC) PickerUtils.getMusicFileTypes() else PickerUtils.getDocFileTypes() ,
                application = activity!!.application,
                comparator = PickerConstant.sortingType.comparator,
                mediaType = fileType!!).map { dir ->
                if (medias.size < 80000){
                    medias.addAll(dir.value)
                }
            }
            // medias.sortWith(Comparator { a, b -> (b.id - a.id).toInt() })
            adapterOthersDataObserver?.setData(medias)
            registerContentObserver()

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_media, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MediaFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(fileType: Int) =
            MediaFragment().apply {
                arguments = Bundle().apply {
                    putInt(FILE_TYPE, fileType)

                }
            }
    }
}