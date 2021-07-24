package com.example.todo.report

import android.content.Context
import android.net.Uri
import com.example.todo.R
import com.example.todo.di.AppScope
import com.example.todo.repo.TodoModel
import com.github.jknack.handlebars.Handlebars
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RosterReport @Inject constructor(
    @ApplicationContext val context: Context,
    engine: Handlebars,
    @AppScope val appScope: CoroutineScope
) {
    private val template = engine.compileInline(context.getString(R.string.report_template))

    suspend fun generate(content: List<TodoModel>, doc: Uri) {
        withContext(Dispatchers.IO + appScope.coroutineContext) {
            context.contentResolver.openOutputStream(doc, "rwt")?.writer()?.use { osw ->
                osw.write(template.apply(content))
                osw.flush()
            }
        }
    }
}
