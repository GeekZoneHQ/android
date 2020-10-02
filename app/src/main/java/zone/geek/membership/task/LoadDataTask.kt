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

package zone.geek.membership.task

import android.content.Context
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.text.HtmlCompat
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import zone.geek.membership.`interface`.NetworkResponseListener
import zone.geek.membership.fragments.LoadDataFragment
import zone.geek.membership.model.Event
import zone.geek.membership.utility.Util.setProgressDialog
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class LoadDataTask(private var fragment: LoadDataFragment) : RssAsyncTask<Int, Void, Exception?>() {
    private lateinit var eventsList: ArrayList<Event>
    private lateinit var dialog: AlertDialog
    private val networkResponseListener: NetworkResponseListener get() = fragment

    companion object {
        private const val RSS_URL = "https://www.meetup.com/GeekZoneCoventry/events/rss/"
        private const val XML_NODE_ITEM = "item"
        private const val XML_NODE_TITLE = "title"
        private const val XML_NODE_LINK = "guid"
        private const val XML_NODE_DESCRIPTION = "description"
        const val PROGRESS_TEXT = "Loading.."
        private const val LINE_SEPARATOR = "line.separator"
        private const val PARSER_PATTERN = "EEEE, MMMM dd 'at' hh:mm aaa"
        private const val FORMATTER_PATTERN = "EEEE, MM/dd hh:mm aaa"
    }

    private var exception: Exception? = null

    override fun onPreExecute() {
        super.onPreExecute()
        val context = fragment.context as Context
        dialog = PROGRESS_TEXT.setProgressDialog(context)
        dialog.show()
    }

    override fun doInBackground(vararg params: Int?): Exception? {

        eventsList = ArrayList()
        var inputStream: InputStream? = null

        try {

            val url = URL(RSS_URL)
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = false
            val xpp = factory.newPullParser()
            inputStream = url.openConnection().getInputStream()
            xpp.setInput(inputStream, "UTF_8")
            var insideItem = false
            var event = Event()
            var eventType = xpp.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (insideItem &&
                    event.isNotEmpty()
                ) {
                    eventsList.add(event)
                    event = Event()
                }
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.name.equals(XML_NODE_ITEM, true)) {
                        insideItem = true
                    } else if (xpp.name.equals(XML_NODE_TITLE, true)) {
                        if (insideItem) {
                            //titles.add(xpp.nextText())
                            event.title = xpp.nextText()
                        }
                    } else if (xpp.name.equals(XML_NODE_LINK, true)) {
                        if (insideItem) {
                            // links.add(xpp.nextText())
                            event.link = xpp.nextText()
                        }
                    } else if (xpp.name.equals(XML_NODE_DESCRIPTION, true)) {
                        if (insideItem) {
                            // descriptions.add(stripHtml(xpp.nextText()))

                            val date = stripHtml(xpp.nextText())
                            event.date = date
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG && xpp.name.equals(
                        XML_NODE_ITEM,
                        true
                    )
                ) {
                    insideItem = false
                }
                eventType = xpp.next()
            }
        } catch (e: MalformedURLException) {
            exception = e
        } catch (e: XmlPullParserException) {
            exception = e
        } catch (e: IOException) {
            exception = e
        } finally {
            inputStream?.close()
        }

        return exception
    }

    override fun onPostExecute(result: Exception?) {
        super.onPostExecute(result)

        if (result == null) {
            networkResponseListener.successData(eventsList)
        } else {
            networkResponseListener.failedData()
        }

        dialog.dismiss()
    }

    private fun getDate(content: String): String {
        return try {
            val parser = SimpleDateFormat(PARSER_PATTERN, Locale.ENGLISH)
            val formatter = SimpleDateFormat(FORMATTER_PATTERN, Locale.ENGLISH)
            val formattedDate = parser.parse(content) as Date
            formatter.format(formattedDate)
        } catch (e: ParseException) {
            Log.d("TAG", e.message as String)
            ""
        }
    }

    private fun stripHtml(html: String): String {
        val newLine = System.getProperty(LINE_SEPARATOR)

        val htmlArray =
            TextUtils.split(
                HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
                    .toString(), newLine
            )
        var eventDate = ""
        htmlArray.forEach {
            val date = getDate(it)
            if (date.isNotEmpty()) {
                eventDate = date
            }
        }
        return eventDate
    }

}