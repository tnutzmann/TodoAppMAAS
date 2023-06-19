package de.thb.fbi.msr.maus.einkaufsliste.remote;

import de.thb.fbi.msr.maus.einkaufsliste.model.User;
import de.thb.fbi.msr.maus.einkaufsliste.model.UserCRUDAccessor;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class RemoteUserAccessor implements UserCRUDAccessor {
    protected static Logger logger = Logger.getLogger(RemoteTodoItemAccessor.class);

    private static final List<User> sUserList = new ArrayList<User>();

    public RemoteUserAccessor() {
        if(sUserList.size() == 0) {
            sUserList.add(new User(0, "tony@thb.de", "123456"));
        }
    }

    @Override
    public boolean checkPassword(User user) {
        logger.info("checkPassword: " + user.toString());

        for(User u : sUserList) {
            if(u.getEmail().compareTo(user.getEmail()) == 0) {
                if(u.getPassword().compareTo(user.getPassword()) == 0) return true;
            }
        }
        return false;
    }
}
