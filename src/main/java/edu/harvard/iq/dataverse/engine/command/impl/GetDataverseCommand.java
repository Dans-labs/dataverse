package edu.harvard.iq.dataverse.engine.command.impl;

import edu.harvard.iq.dataverse.Dataverse;
import edu.harvard.iq.dataverse.authorization.Permission;
import edu.harvard.iq.dataverse.authorization.users.User;
import edu.harvard.iq.dataverse.engine.command.AbstractCommand;
import edu.harvard.iq.dataverse.engine.command.CommandContext;
import edu.harvard.iq.dataverse.engine.command.RequiredPermissions;
import edu.harvard.iq.dataverse.engine.command.exception.CommandException;

/**
 *
 * @author Naomi
 */
@RequiredPermissions( Permission.Discover )
public class GetDataverseCommand extends AbstractCommand<Dataverse>{
    private final Dataverse dv;

    public GetDataverseCommand(User aUser, Dataverse anAffectedDataverse) {
        super(aUser, anAffectedDataverse);
        dv = anAffectedDataverse;
    }

    @Override
    public Dataverse execute(CommandContext ctxt) throws CommandException {
        return dv;
    }
    
}
