package tests;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({"tests.BusinessLogic_and_ORM_Test", "tests.DomainModelTest"})

public class TestRunner {
}
