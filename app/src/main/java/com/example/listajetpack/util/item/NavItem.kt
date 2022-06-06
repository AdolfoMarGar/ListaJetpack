package com.example.listajetpack.util.item

import com.example.listajetpack.R

sealed class NavItem(var label: String, var img: Int )
{
    object Home : NavItem("Home", R.drawable.casa_24)
    object Anadir : NavItem("Añadir", R.drawable.agregar_24)
    object Borrar : NavItem("Borrar", R.drawable.basura_24)


}