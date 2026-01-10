package main.java.view;

/**
 * this interface handles actions triggered from the buttons in the Questions table
 */
public interface TableActionListener {
    void onEdit(int modelRow);
    void onDelete(int modelRow);
}
