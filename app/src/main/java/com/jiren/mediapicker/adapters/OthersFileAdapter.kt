package com.jiren.mediapicker.adapters

import android.content.Context
import android.media.MediaMetadataRetriever
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.jiren.mediapicker.utils.PickerUtils.formatDurationLongToString
import com.jiren.mediapicker.R
import com.jiren.mediapicker.domain.models.FileGenericModel


class OthersFileAdapter (
    private val context: Context,
    private val glide: RequestManager,
    private var type: Int) :
    RecyclerView.Adapter<OthersFileAdapter.MediaViewHolder>()  {
    private var imageSize: Int = 0
    private var items: MutableList<FileGenericModel> = arrayListOf()
    init {
        setColumnNumber(context, 2)
    }

    fun setData(filesMap: List<FileGenericModel>) {
        items.addAll(filesMap)
        notifyDataSetChanged()
    }


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


    /**
     * Metodo para asignar iconos, duracion, tamaño de los archivos
     * @param holder clase con controles para setear
     * @param position segun el scroll del usuario cambia para seguir con el render
     */
    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        try {
            val media = items[position]
            holder.rlMedia.visibility = View.GONE
            holder.view.visibility = View.VISIBLE

            if(media.fileType?.extensions?.contains("mp3") == true){
                var metaRetriever: MediaMetadataRetriever = MediaMetadataRetriever()
                val path = media.path.toString()
                metaRetriever.setDataSource(holder.imageView.context, media.path)
                var duration:String = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                holder.txtTitle.text = formatDurationLongToString(duration)
                holder.txtSubTitle.text = media.name
            }else {
                holder.txtTitle.text = media.size
                holder.txtSubTitle.text = media.name
            }


            holder.imgIcon.setImageDrawable( ContextCompat.getDrawable(
                    holder.imageView.context, // Context
                    media.fileType!!.drawable // Drawable
            ))

        }catch (e: Exception) {
            e.printStackTrace()
        }

    }



    class MediaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imageView: ImageView
        var placeHolder: ImageView
        var txtDuration: TextView
        var view: RelativeLayout
        var txtTitle: TextView
        var txtSubTitle: TextView
        var rlMedia: RelativeLayout
        var imgIcon: ImageView
        init {
            imageView = itemView.findViewById<View>(R.id.imgMedia) as ImageView
            imgIcon = itemView.findViewById<View>(R.id.imgIcon) as ImageView
            placeHolder = itemView.findViewById<View>(R.id.imgShape) as ImageView
            txtDuration = itemView.findViewById<View>(R.id.txtDuration) as TextView
            txtDuration = itemView.findViewById<View>(R.id.txtDuration) as TextView
            view = itemView.findViewById<View>(R.id.viewOthers) as RelativeLayout
            rlMedia = itemView.findViewById<View>(R.id.rlMedia) as RelativeLayout
            txtTitle = itemView.findViewById<View>(R.id.txtTitle) as TextView
            txtSubTitle = itemView.findViewById<View>(R.id.txtSubTitle) as TextView
        }
    }

    override fun getItemCount(): Int {
        return  items.size
    }


}