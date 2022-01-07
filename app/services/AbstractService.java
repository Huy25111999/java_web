package services;

import vn.m2m.common.dao.UserAdminDAO;
import play.Logger;
import vn.m2m.common.dao.UserDAO;

import javax.inject.Inject;

public class AbstractService {
    Logger.ALogger logger = Logger.of(AbstractService.class);
    @Inject
    public UserDAO userDAO;
    @Inject
    public UserAdminDAO userAdminDAO;
}
