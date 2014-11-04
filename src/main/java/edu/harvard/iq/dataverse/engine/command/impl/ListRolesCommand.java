package edu.harvard.iq.dataverse.engine.command.impl;

import edu.harvard.iq.dataverse.Dataverse;
import edu.harvard.iq.dataverse.DvObject;
import edu.harvard.iq.dataverse.authorization.DataverseRole;
import edu.harvard.iq.dataverse.authorization.Permission;
import edu.harvard.iq.dataverse.authorization.users.User;
import edu.harvard.iq.dataverse.engine.command.AbstractCommand;
import edu.harvard.iq.dataverse.engine.command.CommandContext;
import edu.harvard.iq.dataverse.engine.command.RequiredPermissions;
import edu.harvard.iq.dataverse.engine.command.exception.CommandException;
import java.util.Set;

/**
 * List roles defined at a the given {@link DvObject}.
 * @author michael
 */
@RequiredPermissions( Permission.AssignRole )
public class ListRolesCommand extends AbstractCommand<Set<DataverseRole>> {
    
    private final Dataverse definitionPoint;
    
    public ListRolesCommand(User aUser, Dataverse aDefinitionPoint) {
        super(aUser, aDefinitionPoint);
        definitionPoint = aDefinitionPoint;
    }

    @Override
    public Set<DataverseRole> execute(CommandContext ctxt) throws CommandException {
        return definitionPoint.getRoles();
    }

    
}
