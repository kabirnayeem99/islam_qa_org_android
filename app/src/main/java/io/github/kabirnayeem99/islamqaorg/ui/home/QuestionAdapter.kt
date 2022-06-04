package io.github.kabirnayeem99.islamqaorg.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.kabirnayeem99.islamqaorg.common.utility.ktx.viewVisibility
import io.github.kabirnayeem99.islamqaorg.databinding.ListQuestionBinding
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question


class QuestionAdapter : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

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

    inner class QuestionViewHolder(val binding: ListQuestionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)

        val binding = ListQuestionBinding.inflate(layoutInflater, parent, false)

        return QuestionViewHolder(binding)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {

        val currentQuestion = getQuestionAtIndex(position)

        holder.binding.apply {

            question = currentQuestion
            executePendingBindings()

            if (onClick != null) root.setOnClickListener { onClick!!(currentQuestion) }


            val fiqhVisibilityBasedOnFiqhAvailability =
                if (currentQuestion.fiqh.isBlank()) View.GONE
                else View.VISIBLE

            tvSelectedFiqh.viewVisibility(fiqhVisibilityBasedOnFiqhAvailability)
        }
    }

    private fun getQuestionAtIndex(index: Int): Question = differ.currentList[index] ?: Question()

}