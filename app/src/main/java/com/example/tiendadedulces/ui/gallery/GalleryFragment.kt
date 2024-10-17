package com.example.tiendadedulces.ui.gallery

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tiendadedulces.ArticuloActivity
import com.example.tiendadedulces.R
import com.example.tiendadedulces.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textGallery
        galleryViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Datos iniciales
        val detalle: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc nisi dolor, dictum nec mauris et, convallis sollicitudin nisi. Cras eu elit lectus. Suspendisse non arcu in metus facilisis sollicitudin."
        var costo: String = "$175.00"
        var intent: Intent

        //Instancias por imagen
        binding.imgDulce01.setOnClickListener { //1
            Toast.makeText(activity,"${getString(R.string.dulce01)} - $12 MXN por pieza.",
                Toast.LENGTH_SHORT).show()
            costo = "$12 MXN por pieza."
            intent = Intent(activity, ArticuloActivity::class.java)
            intent.putExtra("nombre",getString(R.string.dulce01))
            intent.putExtra("detalle",detalle)
            intent.putExtra("costo", costo)
            intent.putExtra("costoDouble", 12.0)
            intent.putExtra("numAccesorio", 1)
            startActivity(intent)
        }
        binding.imgDulce02.setOnClickListener { //2
            Toast.makeText(activity,"${getString(R.string.dulce02)}  – $25 MXN por porción (100g).",
                Toast.LENGTH_SHORT).show()
            costo = "$25 MXN por porción (100g)."
            intent = Intent(activity, ArticuloActivity::class.java)
            intent.putExtra("nombre",getString(R.string.dulce02))
            intent.putExtra("detalle",detalle)
            intent.putExtra("costo", costo)
            intent.putExtra("costoDouble", 25.0)
            intent.putExtra("numAccesorio", 2)
            startActivity(intent)
        }
        binding.imgDulce03.setOnClickListener { //3
            Toast.makeText(activity,"${getString(R.string.dulce03)} – $18 MXN por pieza.",
                Toast.LENGTH_SHORT).show()
            costo = "$18 MXN por pieza."
            intent = Intent(activity, ArticuloActivity::class.java)
            intent.putExtra("nombre",getString(R.string.dulce03))
            intent.putExtra("detalle",detalle)
            intent.putExtra("costo", costo)
            intent.putExtra("costoDouble", 18.0)
            intent.putExtra("numAccesorio", 3)
            startActivity(intent)
        }
        binding.imgDulce04.setOnClickListener { //4
            Toast.makeText(activity,"${getString(R.string.dulce04)} – $10 MXN por pieza.",
                Toast.LENGTH_SHORT).show()
            costo = "$10 MXN por pieza."
            intent = Intent(activity, ArticuloActivity::class.java)
            intent.putExtra("nombre",getString(R.string.dulce04))
            intent.putExtra("detalle",detalle)
            intent.putExtra("costo", costo)
            intent.putExtra("costoDouble", 10.0)
            intent.putExtra("numAccesorio", 4)
            startActivity(intent)
        }
        binding.imgDulce05.setOnClickListener { //5
            Toast.makeText(activity,"${getString(R.string.dulce05)} – $15 MXN por barra.",
                Toast.LENGTH_SHORT).show()
            costo = "$15 MXN por barra."
            intent = Intent(activity, ArticuloActivity::class.java)
            intent.putExtra("nombre",getString(R.string.dulce05))
            intent.putExtra("detalle",detalle)
            intent.putExtra("costo", costo)
            intent.putExtra("costoDouble", 15.0)
            intent.putExtra("numAccesorio", 5)
            startActivity(intent)
        }
    }//onViewCreated

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}