package edu.hawaii.senin.stack.location;

import java.io.IOException;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import edu.hawaii.senin.stack.db.StackDB;
import edu.hawaii.senin.stack.db.StackDBManager;
import edu.hawaii.senin.stack.persistence.Location;
import edu.hawaii.senin.stack.persistence.User;

public class UserLocationHandler implements ResultHandler {

  private StackDB db;

  public UserLocationHandler() throws IOException {
    super();
    db = StackDBManager.getProductionInstance();
  }

  @Override
  public void handleResult(ResultContext context) {

    User user = (User) context.getResultObject();

    // get the normalized location string
    try {

      String normalLocation = GeocodingFactory.tokenizeNormalize(user.getLocation());

      Location loc = db.getLocationForNormalString(normalLocation);

      if (null != loc) {
        db.updateUserLocation(user.getId(), loc.getId());
      }

    }
    catch (IOException e) {

      e.printStackTrace();
    }

  }
}
