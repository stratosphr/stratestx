package visitors.interfaces;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 22:39
 */
public interface IModelVisitable {

    void accept(IModelVisitor visitor);

}
