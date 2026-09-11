package com.example.ai

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

object AzharAiService {

    private val client = OkHttpClient()

    private fun getApiKey(): String {
        return try {
            val field = BuildConfig::class.java.getField("GEMINI_API_KEY")
            field.get(null) as? String ?: ""
        } catch (e: Exception) {
            try {
                // fallback check if defined as property or empty
                ""
            } catch (ex: Exception) {
                ""
            }
        }
    }

    suspend fun askAzharTeacher(question: String): String = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            // Provide a helpful educational response even without API key, or prompt user
            return@withContext "أهلاً بك يا بطل! بصفتي معلمك الأزهري للصف الثاني الإعدادي الأزهري (2027)، أجيبك على سؤالك: '$question'\n\nإجابة مختصرة من المنهج الأزهري: يُرجى الحرص على مذاكرة النصوص والأبواب المقررة بتركيز. (ملاحظة: لتفعيل الذكاء الاصطناعي الكامل، يرجى ضبط مفتاح Gemini API في لوحة الأمان)."
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
            
            val systemInstruction = "أنت معلم أزهري خبير ومحبوب لمناهج الصف الثاني الإعدادي الأزهري (لعام 2027). أجب عن سؤال الطالب التالي بطريقة واضحة ومبسطة ومفصلة تراعي المنهج الأزهري (أصول الدين، الفقه، النحو، الصرف، التجويد، السيرة)."
            val fullPrompt = "$systemInstruction\n\nسؤال الطالب: $question"

            val jsonBody = JSONObject().apply {
                put("contents", org.json.JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", org.json.JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", fullPrompt)
                            })
                        })
                    })
                })
            }

            val body = jsonBody.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext "حدث خطأ في الاتصال بالخادم (${response.code}). يرجى التحقق من المفتاح."
                }
                val responseString = response.body?.string() ?: return@withContext "استجابة فارغة من الخادم."
                val jsonResponse = JSONObject(responseString)
                val candidates = jsonResponse.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val content = candidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        return@withContext parts.getJSONObject(0).optString("text", "لم يتم العثور على إجابة.")
                    }
                }
                return@withContext "عذراً، لم أتمكن من صياغة إجابة مناسبة."
            }
        } catch (e: Exception) {
            "حدث خطأ أثناء الاتصال بالمعلم الذكي: ${e.localizedMessage}"
        }
    }
}
