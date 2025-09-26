package otus.homework.flowcats

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    override fun populate(result: Result) {
        when (result) {
            is Result.Success<*> -> {
                if (result.data is Fact) {
                    findViewById<TextView>(R.id.fact_textView).text = result.data.fact
                }
            }

            is Result.Error -> {
                Toast.makeText(context, result.msg, Toast.LENGTH_SHORT).show()
            }

            else -> {
                //DO nothing
            }
        }
    }
}

interface ICatsView {

    fun populate(result: Result)
}