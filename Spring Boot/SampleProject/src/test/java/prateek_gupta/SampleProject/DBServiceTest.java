package prateek_gupta.SampleProject;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import prateek_gupta.SampleProject.db.dao.Table1Repository;
import prateek_gupta.SampleProject.db.entities.Table1Entity;
import prateek_gupta.SampleProject.db.service.DBService;
import prateek_gupta.SampleProject.db.service.impl.DBServiceImpl;
import prateek_gupta.SampleProject.db.vo.Table1VO;

public class DBServiceTest {

    // Step 1: Mock the Table1Repository
    @Mock
    private Table1Repository table1Repository;

    // Step 2: Inject mocks into the service
    @InjectMocks
    private DBService DBService = new DBServiceImpl();

    // Step 3: Initialize mocks before each test
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTable1Details_ValidPrimaryKey_ReturnsVO() {
        // Arrange (Set up mock behavior and test data)
        Integer primaryKey = 1;
        Table1Entity mockEntity = Mockito.mock(Table1Entity.class);
        Table1VO expectedVO = new Table1VO();
        Mockito.when(mockEntity.toVO()).thenReturn(expectedVO);
        Mockito.when(table1Repository.findByPrimaryKey(primaryKey)).thenReturn(mockEntity);

        // Act (Call the method under test)
        Table1VO actualVO = DBService.getTable1Details(primaryKey);

        // Assert (Verify the results)
        Assertions.assertNotNull(actualVO);
        Assertions.assertEquals(expectedVO, actualVO);
        Mockito.verify(table1Repository, Mockito.times(1)).
                findByPrimaryKey(primaryKey);
    }

    @Test
    public void testGetTable1Details_InvalidPrimaryKey_ReturnsNull() {
        // Arrange
        Integer invalidPrimaryKey = 999;
        Mockito.when(table1Repository.findByPrimaryKey(invalidPrimaryKey)).thenReturn(null);

        // Act
        Table1VO result = DBService.getTable1Details(invalidPrimaryKey);

        // Assert
        Assertions.assertNull(result);
        Mockito.verify(table1Repository, Mockito.times(1)).
                findByPrimaryKey(invalidPrimaryKey);
    }

    @Test
    public void testGetTable1Details_ExceptionHandling_ReturnsNull() {
        // Arrange
        Integer primaryKey = 1;
        Mockito.when(table1Repository.findByPrimaryKey(primaryKey)).thenThrow(
                new RuntimeException("Database error"));

        // Act
        Table1VO result = DBService.getTable1Details(primaryKey);

        // Assert
        Assertions.assertNull(result);
        Mockito.verify(table1Repository, Mockito.times(1)).
                findByPrimaryKey(primaryKey);
    }
}
