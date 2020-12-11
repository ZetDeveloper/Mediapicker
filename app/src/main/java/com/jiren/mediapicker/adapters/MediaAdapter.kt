package com.jiren.mediapicker.adapters

import android.content.Context
import android.graphics.Rect
import android.media.MediaMetadataRetriever
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.jiren.mediapicker.utils.AndroidLifecycleUtils
import com.jiren.mediapicker.PickerConstant
import com.jiren.mediapicker.utils.PickerUtils
import com.jiren.mediapicker.R
import com.jiren.mediapicker.domain.models.FileModel
import java.util.*

class SpacesItemDecoration(space: Int) : ItemDecoration() {
    private val halfSpace: Int = space / 2

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.paddingLeft != halfSpace) {
            parent.setPadding(halfSpace, halfSpace, halfSpace, halfSpace)
            parent.clipToPadding = false
        }
        outRect.top = halfSpace
        outRect.bottom = halfSpace
        outRect.left = halfSpace
        outRect.right = halfSpace
    }

}

class MediaAdapter(
    private val context: Context,
    private val glide: RequestManager
) :
    RecyclerView.Adapter<MediaAdapter.MediaViewHolder>()  {
    private var imageSize: Int = 0
    private var items: MutableList<FileModel> = arrayListOf()
    init {
        setColumnNumber(context, 2)
    }

    fun setData(items: List<FileModel>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    /**
     * Metodo para obtener el tamaños de las columnas
     * @param columnNum por default2
     */
    private fun setColumnNumber(context: Context, columnNum: Int) {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(metrics)
        val widthPixels = metrics.widthPixels
        imageSize = widthPixels / columnNum
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val itemView = LayoutInflater.from(context).inflate(
                R.layout.item_media_layout,
            parent,
            false
        )

        return MediaViewHolder(itemView)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun Random.nextInt(range: IntRange): Int {
        return range.start + nextInt(range.last - range.start)
    }

    /**
     * Asignaciones de duracion, imagen, nombre
     */
    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) { val random = Random()
       val r  = random.nextInt(0..20000)
        try {
            val media = items[position]
            if (AndroidLifecycleUtils.canLoadImage(holder.imageView.context)) {

                //Cargo imagen
                glide.load(media.path)
                    .apply(
                        RequestOptions
                            .centerCropTransform()
                            .override(
                                imageSize,
                                    //Calculo simple para tratar de mantener el aspec ratio de el zepplin
                                if (r < 10000) ((imageSize * 2) * 0.8).toInt() else ((imageSize * 2) * 0.6).toInt()
                            )
                            .placeholder(R.drawable.imagen)
                    )
                    .thumbnail(0.5f)
                    .into(holder.imageView)

                if(media.mediaType == PickerConstant.MEDIA_TYPE_VIDEO) {
                    holder.placeHolder.visibility = View.VISIBLE
                    holder.txtDuration.visibility = View.VISIBLE

                    //Obtengo metadato de duracion
                    var metaRetriever: MediaMetadataRetriever = MediaMetadataRetriever()
                    val path = media.path.toString()
                    metaRetriever.setDataSource(holder.imageView.context, media.path)
                    var duration:String = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                    holder.txtDuration.text = PickerUtils.formatDurationLongToString(duration)
                }
            }
        }catch (e: Exception) {
            e.printStackTrace()
        }



    }

    class MediaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imageView: ImageView
        var placeHolder: ImageView
        var txtDuration: TextView

        init {
            imageView = itemView.findViewById<View>(R.id.imgMedia) as ImageView
            placeHolder = itemView.findViewById<View>(R.id.imgShape) as ImageView
            txtDuration = itemView.findViewById<View>(R.id.txtDuration) as TextView
        }
    }

    override fun getItemCount(): Int {
        return  items.size
    }


}