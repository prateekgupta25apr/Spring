package prateek_gupta.SampleProject.core;

import com.fasterxml.jackson.databind.node.ObjectNode;
import prateek_gupta.SampleProject.prateek_gupta.ServiceException;

public interface CoreService {
    ObjectNode test(String testData) throws ServiceException;
}
