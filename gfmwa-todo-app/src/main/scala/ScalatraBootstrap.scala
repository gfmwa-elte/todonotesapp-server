import hu.gfmwa.todoapp._
import hu.gfmwa.todoapp.data._
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle with DatabaseInit {

  override def init(context: ServletContext) {
    configureDb()
    context.mount(new TodoScalatraServlet, "/*")
  }

  override def destroy(context:ServletContext) {
    closeDbConnection()
  }
  
}
