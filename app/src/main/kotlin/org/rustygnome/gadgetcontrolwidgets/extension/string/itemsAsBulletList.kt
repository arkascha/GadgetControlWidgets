package org.rustygnome.gadgetcontrolwidgets.extension.string

import android.text.SpannableString
import android.text.style.BulletSpan
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.LinearLayout.VERTICAL
import android.widget.TextView
import org.rustygnome.gadgetcontrolwidgets.R

fun LinearLayout.addItemsAsBulletList(
    items: List<String>,
) {
    this.orientation = VERTICAL

    items.forEach { item ->
        with(this as ViewGroup) {
            TextView(context).apply {
                text = SpannableString(item).apply {
                    setSpan(BulletSpan(15), 0, item.length, 0)
                }
                setTextAppearance(R.style.Theme_GadgetControlWidgets_TextAppearance)
            }.also{
                addView(it)
            }
        }
    }
}