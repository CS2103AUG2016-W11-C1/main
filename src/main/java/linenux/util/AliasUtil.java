package linenux.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores aliases for commands.
 */
public class AliasUtil {
    public static final Map<String, String> ALIASMAP;
    static
    {
      ALIASMAP = new HashMap<String, String>();
      ALIASMAP.put("add", "add");
      ALIASMAP.put("alias", "alias");
      ALIASMAP.put("delete", "delete");
      ALIASMAP.put("done", "done");
      ALIASMAP.put("edit", "edit");
      ALIASMAP.put("exit", "exit");
      ALIASMAP.put("freetime", "freetime");
      ALIASMAP.put("help", "help");
      ALIASMAP.put("list", "list");
      ALIASMAP.put("remind", "remind");
      ALIASMAP.put("undo", "undo");
      ALIASMAP.put("view", "view");
    }
}
