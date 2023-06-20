package com.example.todoviews.accessor;

import android.util.Log;
import com.example.todoviews.models.User;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;

public class UserCRUDAccessor implements IUserCRUDAccessor{
    protected static String logger = TodoItemCRUDAccessor.class.getName();
    private IUserCRUDAccessor restClient;

    public UserCRUDAccessor(String url) {
        Log.i(logger, "initialising restClient for URL: " + url);

        this.restClient = ProxyFactory.create(IUserCRUDAccessor.class, url, new ApacheHttpClient4Executor());
        Log.i(logger,"initialised restClient: " + restClient + " of class " + restClient.getClass());
    }

    @Override
    public boolean checkPassword(User user) {
        Log.i(logger, "checkPassword: "+ user);

        return restClient.checkPassword(user);
    }
}
