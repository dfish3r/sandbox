import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.naming.NamingException;
import edu.vt.middleware.ldap.auth.SearchDnResolver;

public class CustomDnResolver extends SearchDnResolver
{
  public String resolve(final String user)
    throws NamingException
  {
    if (user.contains("@")) {
      final String[] args = user.split("@");
      final List<Object> filterArgs = new ArrayList<Object>();
      filterArgs.add(args[1]);
      if (config.getUserFilterArgs() != null) {
        filterArgs.addAll(Arrays.asList(config.getUserFilterArgs()));
      }
      config.setUserFilterArgs(
        filterArgs.toArray(new Object[filterArgs.size()]));
      return super.resolve(args[0]);
    } else {
      return super.resolve(user);
    }
  }
}

