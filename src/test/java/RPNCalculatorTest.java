import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.test.exception.InsufficientParametersException;
import com.test.impl.RPNCalculator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RPNCalculatorTest {

  private RPNCalculator rpnCalculator = null;

  @BeforeMethod
  public void setUp() {
    this.rpnCalculator = new RPNCalculator();
  }

  @Test
  public void calcShouldBeAbleToCalculateSingleDigitNumbers() {
    assertThat(this.rpnCalculator.calculate("1 2 +"), is(equalTo("3.0")));
  }

  @Test
  public void calcShouldBeAbleToCalculateMultiDigitNumbers() {
    assertThat(this.rpnCalculator.calculate("12 3 /"), is(equalTo("4.0")));
  }

  @Test
  public void calcShouldBeAbleToHandleNegativeNumbers() {
    assertThat(this.rpnCalculator.calculate("-12 3 /"), is(equalTo("-4.0")));
  }

  @Test
  public void calShouldBeAbleToHandleDecimalNumbers() {
    assertThat(this.rpnCalculator.calculate("-12.9 3 /"), is(equalTo("-4.3")));
  }

  @Test
  public void calShouldBeAbleToHandleMoreComplexNotations() {
    assertThat(this.rpnCalculator.calculate("1 2 + 4 * 5 + 3 -"), is(equalTo("14.0")));
  }

  @Test
  public void calShouldBeAbleToHandleClearOperation() {
    assertThat(this.rpnCalculator.calculate("2 sqrt clear 9 sqrt"), is(equalTo("3.0")));
  }

  @Test
  public void calShouldBeAbleToHandleMultiMULTIPLY() {
    assertThat(this.rpnCalculator.calculate(" 1 2 3 4 5 * * * *"), is(equalTo("120.0")));
  }

  @Test
  public void calShouldBeAbleToHandleUndo() {
    assertThat(this.rpnCalculator.calculate("5 4 3 2 undo undo * 5 * undo"), is(equalTo("100.0")));
  }

  @Test(expectedExceptions = {
      InsufficientParametersException.class}, expectedExceptionsMessageRegExp = ".*insucient parameters.*")
  public void calShouldBeAbleToHandleInvalidParameter() {
    assertThat(this.rpnCalculator.calculate("1 2 3 * 5 + * * 6 5"), is(equalTo("100.0")));
  }

}
