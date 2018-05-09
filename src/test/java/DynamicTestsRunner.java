import static org.junit.Assert.fail;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

public class DynamicTestsRunner extends Runner {

    List<String> testCases = new LinkedList<>();

    public DynamicTestsRunner(Class<?> testClass) {
        IntStream.range(0, 20).forEach(i -> testCases.add("Test-" + i));
    }

	@Override
	public Description getDescription() {
        Description description = Description.createSuiteDescription("DynamicTests");
        for(String testCase : testCases) {
            description.addChild(getDescription(testCase));
        }
        return description;
    }
    
    private Description getDescription(String testName) {
        return Description.createTestDescription("DynamicTests", testName);
    }

	@Override
	public void run(RunNotifier notifier) {
        Description description = getDescription();
        notifier.fireTestStarted(description);
        for (String testCase : testCases) {
            invokeTest(notifier, testCase);
        }
        notifier.fireTestFinished(description);
		
    }

    private void invokeTest(RunNotifier notifier, String testCase) {
        Description description = getDescription(testCase);
        notifier.fireTestStarted(description);
        try {
            if (testCase.equals("Test-4")) fail("failed");
            System.out.println("Excecute: " + description);
        } catch (AssertionError e) {
            notifier.fireTestFailure(new Failure(description, e));
        } finally {
            notifier.fireTestFinished(description);
        }
	}
}