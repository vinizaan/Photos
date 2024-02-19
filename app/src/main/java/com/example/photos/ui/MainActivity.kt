package com.example.photos.ui

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Toast
import com.example.photos.R
import com.example.photos.databinding.ActivityMainBinding
import com.example.photos.model.DummyJsonAPI
import com.android.volley.toolbox.ImageRequest
import com.example.photos.adapter.PhotoAdapter
import com.example.photos.model.Photo

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val photoList: MutableList<Photo> = mutableListOf()
    private val photoAdapter: PhotoAdapter by lazy {
        PhotoAdapter(this, photoList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        setSupportActionBar(amb.mainTb.apply {
            title = getString(R.string.app_name)
        })

        amb.photosSp.apply {
            adapter = photoAdapter
            onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    retrievePhotoImages(photoList[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // NSA
                }
            }
        }

        retrievePhotos()
    }

    private fun retrievePhotoImages(photo: Photo) {
        ImageRequest(photo.url,
            {response ->
                amb.photoIv.setImageBitmap(response)
            },
            0,
            0,
            ImageView.ScaleType.CENTER,
            Bitmap.Config.ARGB_8888,
            {
                Toast.makeText(this, getString(R.string.request_problem), Toast.LENGTH_SHORT).show()
            }
        ).also {
            DummyJsonAPI.getInstance(this).addToRequestQueue(it)
        }

        ImageRequest(photo.thumbnailUrl,
            {response ->
                amb.photoThumbnailIv.setImageBitmap(response)
            },
            0,
            0,
            ImageView.ScaleType.CENTER,
            Bitmap.Config.ARGB_8888,
            {
                Toast.makeText(this, getString(R.string.request_problem), Toast.LENGTH_SHORT).show()
            }
        ).also {
            DummyJsonAPI.getInstance(this).addToRequestQueue(it)
        }
    }

    private fun retrievePhotos() =
        DummyJsonAPI.PhotoListRequest(
            { photoList ->
                photoList.also {
                    photoAdapter.addAll(it)
                }
            }, {
                Toast.makeText(this, getString(R.string.request_problem), Toast.LENGTH_SHORT).show()
            }
        ).also {
            DummyJsonAPI.getInstance(this).addToRequestQueue(it)
        }
}