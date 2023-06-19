package de.thb.fbi.msr.maus.einkaufsliste.remote;

import de.thb.fbi.msr.maus.einkaufsliste.model.TodoItem;
import de.thb.fbi.msr.maus.einkaufsliste.model.TodoItemCRUDAccessor;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class RemoteTodoItemAccessor implements TodoItemCRUDAccessor {
    protected static Logger logger = Logger.getLogger(RemoteTodoItemAccessor.class);

    private static final List<TodoItem> sTodoItemList = new ArrayList<TodoItem>();


    @Override
    public List<TodoItem> readAllTodoItems() {
        logger.info("readAllTodoItems(): " + sTodoItemList);

        return sTodoItemList;
    }

    @Override
    public TodoItem createTodoItem(TodoItem todoItem) {
        logger.info("createTodoItem(): " + todoItem);

        sTodoItemList.add(todoItem);
        return todoItem;
    }

    @Override
    public boolean deleteTodoItem(long todoItemId) {
        logger.info("deleteTodoItem(): " + todoItemId);

        TodoItem toDelete;

        for(TodoItem ti : sTodoItemList) {
            if(ti.getId() == todoItemId) return sTodoItemList.remove(ti);
        }
        return false;
    }

    @Override
    public TodoItem updateTodoItem(TodoItem todoItem) {
        logger.info("updateTodoItem(): " + todoItem);

        return sTodoItemList.get(sTodoItemList.indexOf(todoItem)).update(todoItem);
    }
}
