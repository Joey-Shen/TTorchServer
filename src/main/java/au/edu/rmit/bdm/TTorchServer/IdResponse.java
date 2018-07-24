package au.edu.rmit.bdm.TTorchServer;

import au.edu.rmit.bdm.TTorch.base.Torch;
import au.edu.rmit.bdm.TTorch.base.model.Coordinate;
import au.edu.rmit.bdm.TTorch.base.model.TrajEntry;
import au.edu.rmit.bdm.TTorch.queryEngine.query.QueryResult;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

class IdResponse {
    static Gson gson = new Gson();
    private transient Logger logger = LoggerFactory.getLogger(IdResponse.class);
    boolean formatCorrect;
    ResultObj retObj;


    private IdResponse(){
        formatCorrect = false;
    }

    private IdResponse(QueryResult queryResult){
        if (queryResult.isResolved){
            logger.debug("trajectory is resolved, shouldn't happen");
            System.exit(-1);
        }
        formatCorrect = true;
        retObj = new ResultObj();
        retObj.mappingSucceed = queryResult.mappingSucceed;

        if (!queryResult.queryType.equals(Torch.QueryType.RangeQ)) {
            if (!retObj.mappingSucceed) return;
            for (TrajEntry entry : queryResult.mappedQuery)
                retObj.mappedTrajectory.add(new Coordinate(entry.getLat(), entry.getLng()));
        }
        retObj.retSize = queryResult.retSize;
        if (retObj.retSize == 0) return;
        retObj.ids = queryResult.idArray;
    }

    static IdResponse genFailed(){
        return new IdResponse();
    }

    static IdResponse genSuccessful(QueryResult ret){
        return new IdResponse(ret);
    }

    String toJSON(){
        return gson.toJson(this);
    }
}
