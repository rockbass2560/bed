package com.rockbass.bed

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import kotlinx.android.synthetic.main.rafd_view_fragment.*
import ss.com.bannerslider.Slider
import ss.com.bannerslider.adapters.SliderAdapter
import ss.com.bannerslider.event.OnSlideChangeListener
import ss.com.bannerslider.viewholder.ImageSlideViewHolder


class RafdViewFragment : Fragment() {

    private val TAG : String = RafdViewFragment::javaClass.name

    private lateinit var rafdViewModel: RafdViewModel

    private lateinit var openCV : OpenCV

    private val ROOT_FOLDER_RAFD = "rafd_images"

    inner class MainSliderAdapter : SliderAdapter(){

        private val imagenes : Array<String> = context?.assets?.list(ROOT_FOLDER_RAFD) ?: emptyArray()

        override fun getItemCount(): Int {
            return imagenes.size
        }

        override fun onBindImageSlide(position: Int, imageSlideViewHolder: ImageSlideViewHolder?) {
            imageSlideViewHolder?.bindImageSlide("${imagenes[position]}$position")
        }

    }

    //Extensión para imprimir porcentajes en los valores
    fun Float.toPercentage() : String{
       return "%.2f".format(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.rafd_view_fragment, container, false)

        val slider : Slider = view.findViewById(R.id.sliderView)
        slider.setAdapter(MainSliderAdapter())
        slider.onSlideChangeListener = object : OnSlideChangeListener{
            override fun onSlideChange(position: Int) {
                //Traer resultados
                val results = HandleImage.getHandleImage(context as Context).results[position]
                colocarTextos(results.second)
            }
        }

        HandleImage.getHandleImage(context as Context).onChangeResult { result,index ->
            if (slider.selectedSlidePosition==index) {
                //Rostro detectado
                if (result.first) {
                    colocarTextos(result.second)
                }
            }
        }

        return view
    }

    fun colocarTextos(calificaciones : OpenCV.CalificacionEmociones){
        with(calificaciones) {
            textEnojado.text = enojado.toPercentage()
            textFeliz.text = feliz.toPercentage()
            textMiedo.text = miedo.toPercentage()
            textNeutral.text = neutral.toPercentage()
            textSorpresa.text = sorpresa.toPercentage()
            textTriste.text = triste.toPercentage()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rafdViewModel = ViewModelProviders.of(this).get(RafdViewModel::class.java)
        // TODO: Use the ViewModel

    }

}
