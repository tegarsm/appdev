package com.tsm.android.appdev.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tsm.android.appdev.R
import com.tsm.android.appdev.databinding.DevbyteItemBinding
import com.tsm.android.appdev.databinding.FragmentDevByteBinding
import com.tsm.android.appdev.domain.DevByteVideo
import com.tsm.android.appdev.viewmodels.DevByteViewModel

/**
 * pada bagian ini merupakan Tampilkan daftar DevBytes di layar.
 */
class DevByteFragment : Fragment() {

    /**
     * pada bagian ini digunakan untuk menunda pembuatan viewModel hingga metode siklus hidup
     */
    private val viewModel: DevByteViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, DevByteViewModel.Factory(activity.application))
                .get(DevByteViewModel::class.java)
    }

    /**
     * Adaptor RecyclerView untuk mengonversi daftar Video ke kartu.
     */
    private var viewModelAdapter: DevByteAdapter? = null

    /**
     * Dipanggil segera setelah onCreateView() telah kembali, dan milik fragmen
     * hierarki tampilan telah dibuat. Ini dapat digunakan untuk melakukan final
     * inisialisasi setelah potongan-potongan ini terpasang, seperti mengambil
     * melihat atau memulihkan status.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.playlist.observe(viewLifecycleOwner, Observer<List<DevByteVideo>> { videos ->
            videos?.apply {
                viewModelAdapter?.videos = videos
            }
        })
    }

    /**
     * Dipanggil agar fragmen membuat instance tampilan antarmuka penggunanya.
     *
     * <p>Jika Anda mengembalikan View dari sini, Anda akan dipanggil nanti
     * {@link #onDestroyView} saat tampilan dirilis.
     *
     * @param inflater Objek LayoutInflater yang dapat digunakan untuk mengembang
     * setiap tampilan dalam fragmen,
     * @param container Jika bukan nol, ini adalah tampilan induk dari fragmen
     * UI harus dilampirkan. Fragmen tidak boleh menambahkan tampilan itu sendiri,
     * tapi ini bisa digunakan untuk menghasilkan LayoutParams dari tampilan.
     * @param storedInstanceState Jika bukan nol, fragmen ini sedang dibuat ulang
     * dari status tersimpan sebelumnya seperti yang diberikan di sini.
     *
     * @return Mengembalikan Tampilan untuk UI fragmen.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentDevByteBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_dev_byte,
                container,
                false)
        // Atur lifecycleOwner agar DataBinding dapat mengamati LiveData
        binding.setLifecycleOwner(viewLifecycleOwner)

        binding.viewModel = viewModel

        viewModelAdapter = DevByteAdapter(VideoClick {
            // Saat video diklik, blok atau lambda ini akan dipanggil oleh DevByteAdapter

            // konteks tidak ada, kita dapat membuang klik ini dengan aman karena Fragmen tidak ada
            // lebih lama di layar
            val packageManager = context?.packageManager ?: return@VideoClick

            // Coba buat maksud langsung ke aplikasi YouTube
            var intent = Intent(Intent.ACTION_VIEW, it.launchUri)
            if(intent.resolveActivity(packageManager) == null) {
                // aplikasi YouTube tidak ditemukan, gunakan url web
                intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
            }

            startActivity(intent)
        })

        binding.root.findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }


        // Pengamat untuk kesalahan jaringan.
        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })

        return binding.root
    }

    /**
     * Method untuk menampilkan pesan kesalahan Toast untuk kesalahan jaringan.
     */
    private fun onNetworkError() {
        if(!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }

    /**
     * Helper method untuk menghasilkan tautan aplikasi YouTube
     */
    private val DevByteVideo.launchUri: Uri
        get() {
            val httpUri = Uri.parse(url)
            return Uri.parse("vnd.youtube:" + httpUri.getQueryParameter("v"))
        }
}

/**
 * Click listener untuk Video. Dengan memberi nama blok itu membantu pembaca memahami apa yang dilakukannya.
 *
 */
class VideoClick(val block: (DevByteVideo) -> Unit) {
    /**
     * Dipanggil saat video diklik
     *
     * @param video video yang diklik
     */
    fun onClick(video: DevByteVideo) = block(video)
}

/**
 * Adaptor RecyclerView untuk menyiapkan pengikatan data pada item dalam daftar.
 */
class DevByteAdapter(val callback: VideoClick) : RecyclerView.Adapter<DevByteViewHolder>() {

    /**
     * Video yang akan ditampilkan Adaptor kami
     */
    var videos: List<DevByteVideo> = emptyList()
        set(value) {
            field = value
            // Untuk tantangan ekstra, perbarui ini untuk menggunakan perpustakaan paging.

            // Beri tahu semua pengamat terdaftar bahwa kumpulan data telah berubah. Ini akan menyebabkan setiap
            // elemen di RecyclerView kita menjadi tidak valid.
            notifyDataSetChanged()
        }

    /**
     * Dipanggil saat RecyclerView membutuhkan {@link ViewHolder} baru dari jenis tertentu untuk direpresentasikan
     * sebuah benda.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevByteViewHolder {
        val withDataBinding: DevbyteItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                DevByteViewHolder.LAYOUT,
                parent,
                false)
        return DevByteViewHolder(withDataBinding)
    }

    override fun getItemCount() = videos.size

    /**
     * Dipanggil oleh RecyclerView untuk menampilkan data pada posisi yang ditentukan. Metode ini harus
     * perbarui konten {@link ViewHolder#itemView} untuk mencerminkan item yang diberikan
     * posisi.
     */
    override fun onBindViewHolder(holder: DevByteViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.video = videos[position]
            it.videoCallback = callback
        }
    }

}

/**
 * ViewHolder for DevByte items. All work is done by data binding.
 */
class DevByteViewHolder(val viewDataBinding: DevbyteItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.devbyte_item
    }
}