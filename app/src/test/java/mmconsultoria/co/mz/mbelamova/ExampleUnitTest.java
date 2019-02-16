package mmconsultoria.co.mz.mbelamova;

import org.junit.Assert;
import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void observable_map() {
        String name = "name";

        Observable.<String>empty()
                .map(s -> name)
                .doOnNext(next->Assert.assertEquals(name+"",next))
        .subscribe();
    }
}