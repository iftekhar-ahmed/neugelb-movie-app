@file:JvmName("Extensions")

package com.example.moviecatalog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Iftekhar Ahmed on 02/07/2021.
 */

fun ViewGroup.inflate(layoutResId: Int, attachToRoot: Boolean = false): View = LayoutInflater.from(context).inflate(layoutResId, this, attachToRoot)