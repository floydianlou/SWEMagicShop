package tests;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({"tests.BusinessLogicTest", "tests.ORMTest"})

public class TestRunner {
}
