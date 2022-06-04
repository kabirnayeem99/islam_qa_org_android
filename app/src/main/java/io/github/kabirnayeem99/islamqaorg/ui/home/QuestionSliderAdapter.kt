package io.github.kabirnayeem99.islamqaorg.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.kabirnayeem99.islamqaorg.R
import io.github.kabirnayeem99.islamqaorg.databinding.SlideQuestionBinding
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question


class QuestionSliderAdapter : RecyclerView.Adapter<QuestionSliderAdapter.QuestionViewHolder>() {

    private var onClick: ((Question) -> Unit)? = null

    fun setOnClickListener(onClickParam: ((Question) -> Unit)?) {
        onClick = onClickParam
    }

    fun submitQuestionList(questionList: List<Question>) {
        differ.submitList(questionList)
    }

    private val diffUtl = object : DiffUtil.ItemCallback<Question>() {
        override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffUtl)

    inner class QuestionViewHolder(val binding: SlideQuestionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)

        val binding = SlideQuestionBinding.inflate(layoutInflater, parent, false)

        return QuestionViewHolder(binding)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val currentQuestion = getQuestionAtIndex(position)
        holder.binding.apply {
            question = currentQuestion
            executePendingBindings()
            Glide.with(root.context).load(R.drawable.bg_banner).into(ivBackground)
            if (onClick != null) root.setOnClickListener {
                onClick!!(currentQuestion)
            }
        }
    }

    private fun getQuestionAtIndex(index: Int): Question = differ.currentList[index] ?: Question()


}