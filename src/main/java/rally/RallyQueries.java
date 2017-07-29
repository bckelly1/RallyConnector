package rally;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rallydev.rest.RallyRestApi;
import com.rallydev.rest.request.QueryRequest;
import com.rallydev.rest.response.QueryResponse;
import com.rallydev.rest.util.Fetch;
import com.rallydev.rest.util.QueryFilter;

import java.io.IOException;

/**
 * Created by Brian on 1/28/2017.
 */
public class RallyQueries {
    public static void getProjectDefects(String key) {
        RallyRestApi rallyRestApi = RallyConnector.createConnection("https://rally1.rallydev.com", key);
        try {
            System.out.println("Querying for top 5 highest priority unfixed defects...");

            QueryRequest defects = new QueryRequest("defect");
            QueryFilter queryFilter1 = new QueryFilter("State", "<", "Fixed")
//                    .and(new QueryFilter("Owner.UserName","=","kelbr09@ca.com"))
//                    .and(new QueryFilter("Owner.Name.FirstName","=","Brian"))
                    .and(new QueryFilter("c_UIMComponent", "=", "CABI for UIM"));


            defects.setFetch(new Fetch("FormattedID", "Name", "State", "Priority", "Owner", "Project","c_UIMComponent"));
            defects.setQueryFilter(queryFilter1);
            defects.setOrder("Priority DESC,FormattedID ASC");
//            defects.setQueryFilter(new QueryFilter("Owner", "=", "Brian.Kelly@ca.com"));
//            defects.setProject("/project/Blues Brothers");

            //Return up to 5, 1 per page
//            defects.setPageSize(1);
//            defects.setLimit(5);

            QueryResponse queryResponse = rallyRestApi.query(defects);
            if (queryResponse.wasSuccessful()) {
                System.out.println(String.format("\nTotal results: %d", queryResponse.getTotalResultCount()));
                System.out.println("Top 20:");

                String owner = "null";
                String project = "null";
                for (JsonElement result : queryResponse.getResults()) {
                    JsonObject defect = result.getAsJsonObject();
                    if (defect.has("Owner") && !defect.get("Owner").isJsonNull()) {
                        owner = defect.get("Owner").getAsJsonObject().get("_refObjectName").getAsString();
                    }
                    if (defect.has("Project") && !defect.get("Project").isJsonNull()) {
                        project = defect.get("Project").getAsJsonObject().get("Name").getAsString();
                    }
                    System.out.println(String.format("\t%s : Priority=%s, State=%s, Owner=%s, Component=%s, Project=%s",
                            defect.get("FormattedID").getAsString(),
//                            defect.get("Name").getAsString(),
                            defect.get("Priority").getAsString(),
                            defect.get("State").getAsString(),
                            owner,
                            defect.get("c_UIMComponent").getAsString(),
                            project
                    ));
                    owner = "null";
                    project = "null";
                }
            } else {
                System.err.println("The following errors occurred: ");
                for (String err : queryResponse.getErrors()) {
                    System.err.println("\t" + err);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //Release resources
            try {
                rallyRestApi.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void getProjectTestCases(String key) {
        RallyRestApi rallyRestApi = RallyConnector.createConnection("https://rally1.rallydev.com", key);
        try {
            System.out.println("Querying for all Test Cases...");

            QueryRequest testCases = new QueryRequest("testCase");


            testCases.setFetch(new Fetch("FormattedID", "Name", "Method", "Owner", "Project"));
            testCases.setProject("/project/63166290508");//Blues Brothers project ID
            testCases.setOrder("Method ASC");

            float automatedCount = 0;
            float manualCount = 0;
            QueryResponse queryResponse = rallyRestApi.query(testCases);
            if (queryResponse.wasSuccessful()) {
                System.out.println(String.format("\nTotal results: %d", queryResponse.getTotalResultCount()));

                String owner = "null";
                String project = "null";
                for (JsonElement result : queryResponse.getResults()) {
                    JsonObject testCase = result.getAsJsonObject();
                    if (testCase.has("Owner") && !testCase.get("Owner").isJsonNull()) {
                        owner = testCase.get("Owner").getAsJsonObject().get("_refObjectName").getAsString();
                    }
                    if (testCase.has("Project") && !testCase.get("Project").isJsonNull()) {
                        project = testCase.get("Project").getAsJsonObject().get("Name").getAsString();
                    }
                    if(testCase.get("Method").getAsString().equals("Automated")){
                        automatedCount++;
                    }
                    else if(testCase.get("Method").getAsString().equals("Manual")){
                        manualCount++;
                    }
                    System.out.println(String.format("\t%s : Name=%s, Owner=%s, Method=%s, Project=%s",
                            testCase.get("FormattedID").getAsString(),
                            testCase.get("Name").getAsString(),
                            owner,
                            testCase.get("Method").getAsString(),
                            project
                    ));
                    owner = "null";
                    project = "null";
                }

                System.out.println("Manual count: " + manualCount);
                System.out.println("Automated count: " + automatedCount);
                System.out.println("Automated percentage: " + (automatedCount / manualCount) *100 + "%");
            } else {
                System.err.println("The following errors occurred: ");
                for (String err : queryResponse.getErrors()) {
                    System.err.println("\t" + err);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //Release resources
            try {
                rallyRestApi.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
