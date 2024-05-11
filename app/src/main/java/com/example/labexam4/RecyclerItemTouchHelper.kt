import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.labexam4.Adapter.ToDoAdapter
import com.example.labexam4.R

class RecyclerItemTouchHelper(
    private val adapter: ToDoAdapter
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        var position:Int =viewHolder.adapterPosition
        if(direction == ItemTouchHelper.LEFT){
            var builder:AlertDialog.Builder  = AlertDialog.Builder(adapter.getContext())
            builder.setTitle("Delete Task")
            builder.setMessage("Are you sure you want to delete this task?")
            builder.setPositiveButton("Confirm", DialogInterface.OnClickListener { dialog, which ->

                adapter.deleteItem(position)
            })
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->

                adapter.notifyItemChanged(viewHolder.adapterPosition)
            })

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
        else{
            adapter.editItem(position)
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
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        var icon:Drawable
        var background: ColorDrawable

        var itemView: View = viewHolder.itemView
        var backgroundCornerOffSet:Int = 20
        if(dX > 0){
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.edit)!!
            background = ColorDrawable(Color.GREEN)
        }
        else{
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.delete)!!
            background = ColorDrawable(Color.RED)

        }

        val iconMargin:Int = itemView.height - icon.intrinsicHeight / 2
        val iconTop:Int = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
        var iconBottom:Int = iconTop + icon.intrinsicHeight

        if(dX > 0 ){
            val iconLeft :Int = itemView.left + iconMargin
            val iconRight:Int = itemView.left + iconMargin + icon.intrinsicHeight
            icon.setBounds(iconLeft,iconTop,iconRight,iconBottom)

            background.setBounds(itemView.left, itemView.top, itemView.left + dX.toInt() + backgroundCornerOffSet, itemView.bottom)
        }

        else if (dX < 0){
            val iconLeft :Int = itemView.right + iconMargin - icon.intrinsicWidth
            val iconRight:Int = itemView.right - iconMargin
            icon.setBounds(iconLeft,iconTop,iconRight,iconBottom)

            background.setBounds(itemView.right + dX.toInt() - backgroundCornerOffSet, itemView.top, itemView.right , itemView.bottom)
        }
        else{
            background.setBounds(0,0,0,0)
        }
        background.draw(c);
        icon.draw(c);
    }

}
