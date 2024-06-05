package com.servicecops.mentors.permissions;

import com.servicecops.mentors.models.jpahelpers.enums.AppDomains;

/**
 * This class will hold all our permissions we are using in the system
 *
 * It comes with an example of how to register a new permission.
 *
 * NOTE:- If your app does not follow the architecture of domains, you can always pass null as the domain in the last parameter of a permission
 */
public class Permisions {
    Permission ADMINISTRATOR = new Permission("ADMINISTRATOR", "Can administer the system", AppDomains.BACK_OFFICE, true);
    Permission ASSIGNS_PERMISSIONS = new Permission("ASSIGNS_PERMISSIONS", "Can Assign Permissions to roles", AppDomains.BACK_OFFICE, true);
    Permission CLIENT = new Permission("CLIENT", "Can use application", AppDomains.CLIENT_SIDE, false);
    Permission MENTOR =  new Permission("MENTOR", "Can mentor mentees", AppDomains.CLIENT_SIDE, false);
    Permission MENTEE = new Permission("MENTEE", "Can have mentors", AppDomains.CLIENT_SIDE, false);
}
