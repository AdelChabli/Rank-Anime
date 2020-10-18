package com.example.rankanime.modele

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class LeftRightFullSwipeCallback(
    context: Context, private val myAdapter: RvAdapter,
    private val listener: OnSwipeCallback?,

    @DrawableRes
    rightIcon: Int,

    @ColorInt
    rightBgColor: Int,

    @DrawableRes
    leftIcon: Int,

    @ColorInt
    leftBgColor: Int
) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {

    private val rightIcon = ContextCompat.getDrawable(context, rightIcon)!!
    private val rightBackground = ColorDrawable(rightBgColor)
    private var isDrop = false
    private var dragFrom = -1;
    private var dragTo = -1;

    private val leftIcon = ContextCompat.getDrawable(context, leftIcon)!!
    private val leftBackground = ColorDrawable(leftBgColor)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val fromPos = viewHolder.adapterPosition
        val toPos = target.adapterPosition

        if(dragFrom == -1) {
            dragFrom =  fromPos;
        }
        dragTo = toPos;

        myAdapter.onItemMove(viewHolder?.adapterPosition, target?.adapterPosition);
        return true
    }



    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition

        when (direction) {
            ItemTouchHelper.LEFT -> {
                listener?.onSwipeLeft(position)
            }

            ItemTouchHelper.RIGHT -> {
                listener?.onSwipeRight(position)
            }
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView

        val iconMargin = (itemView.height - rightIcon.intrinsicHeight) / 2
        val iconTop =
            itemView.top + (itemView.height - rightIcon.intrinsicHeight) / 2
        val iconBottom = iconTop + rightIcon.intrinsicHeight

        when {
            dX > 0 -> {
                // right swipe
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )

                val iconLeft = itemView.left + iconMargin
                val iconRight = itemView.left + iconMargin + rightIcon.intrinsicWidth
                leftIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                leftBackground.setBounds(
                    itemView.left + dX.toInt(),
                    itemView.top, itemView.left, itemView.bottom
                )

                leftBackground.draw(c)
                leftIcon.draw(c)
            }

            dX < 0 -> {
                // left swipe
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                val iconLeft = itemView.right - iconMargin - rightIcon.intrinsicWidth
                val iconRight = itemView.right - iconMargin
                rightIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                rightBackground.setBounds(
                    itemView.right + dX.toInt(),
                    itemView.top, itemView.right, itemView.bottom
                )

                rightBackground.draw(c)
                rightIcon.draw(c)
            }

            else -> {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                rightBackground.setBounds(0, 0, 0, 0)
                leftBackground.setBounds(0, 0, 0, 0)

                rightBackground.draw(c)
                leftBackground.draw(c)
            }
        }

    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        if(dragFrom != -1 && dragTo != -1 && dragFrom != dragTo) {
            myAdapter.changeRankOrder()
        }

        dragFrom = -1
        dragTo = -1
    }




}