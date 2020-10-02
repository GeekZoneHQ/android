/**
 *   Copyright 2020 Geek Zone UK
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package zone.geek.membership.utility

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import zone.geek.membership.R

object Util {
    @JvmStatic
    fun String.setProgressDialog(context: Context): AlertDialog {
        val linearLayoutPadding = 30
        val textSite = 20F
        val linearLayout = LinearLayout(context)

        linearLayout.orientation = LinearLayout.HORIZONTAL
        linearLayout.setPadding(
            linearLayoutPadding, linearLayoutPadding,
            linearLayoutPadding, linearLayoutPadding
        )
        linearLayout.gravity = Gravity.CENTER

        var linearLayoutParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        linearLayoutParam.gravity = Gravity.CENTER
        linearLayout.layoutParams = linearLayoutParam

        val progressBar = ProgressBar(context)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, linearLayoutPadding, 0)
        progressBar.layoutParams = linearLayoutParam

        linearLayoutParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        linearLayoutParam.gravity = Gravity.CENTER
        val tvText = TextView(context)
        tvText.text = this
        tvText.setTextColor(ContextCompat.getColor(context, R.color.progressDialog))
        tvText.textSize = textSite
        tvText.layoutParams = linearLayoutParam

        linearLayout.addView(progressBar)
        linearLayout.addView(tvText)

        val builder = AlertDialog.Builder(context)
        builder.setCancelable(true)
        builder.setView(linearLayout)

        val dialog = builder.create()
        val window = dialog.window
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog.window?.attributes = layoutParams
        }
        return dialog
    }
}