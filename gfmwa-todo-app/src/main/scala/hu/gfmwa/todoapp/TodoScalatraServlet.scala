package hu.gfmwa.todoapp

import org.scalatra._
import scalate.ScalateSupport
import hu.gfmwa.todoapp.data._
import hu.gfmwa.todoapp.models._
import org.squeryl.PrimitiveTypeMode._
import scala.util.Random
import java.util.Collections

class TodoScalatraServlet extends GfmwaTodoAppStack {

  get("/") {
  	"Hello Stranger"
  }

  get("/todos") {
    contentType = formats("json")
    
    val userid : Int = params.getOrElse("userid", halt(400)).toInt
    val limit : Int = params.getOrElse("limit", "-1").toInt
    val offset : Int = params.getOrElse("offset", "-1").toInt


    if(limit == -1 || offset == -1) 	    
		from(TodoDb.todos)(todo => where(todo.userId === 1) select(todo)).toList
	else {
 		from(TodoDb.todos)(todo => where(todo.userId === userid) select(todo) orderBy(todo.modified)).page(offset, limit).toList
	}
  }
  
  post("/register") {
	contentType = formats("json")
  	val person = parsedBody.extract[RegInfo]
	if(from(UserDb.users)(user => where(user.username === person.username) select(user)).size == 0) {
		if(from(UserDb.users)(user => where(user.mailaddress === person.mailaddress) select(user)).size == 0) {
			val user = new User(0, person.username, person.mailaddress, person.password)
			if(User.create(user)) {
				"{\"error\" : false}"
			} else {
				"{\"error\" : true, \"errorMessages\" : {\"hu\": \"A regisztráció sikertelen, kérem próbálja meg később!\", \"en\" : \"The registration is unsuccessful, please try again later!\"}}"
			}
		} else {
			"{\"error\" : true, \"errorMessages\" : {\"hu\": \"A regisztráció sikertelen, a megadott e-mail cím foglalt!\", \"en\" : \"The registration is unsuccessful, the given e-mail address is taken!\"}}"
		}
	} else {
		"{\"error\" : true, \"errorMessages\" : {\"hu\": \"A regisztráció sikertelen, a kért felhasználónév foglalt!\", \"en\" : \"The registration is unsuccessful, the username is taken!\"}}"
	}
  }
  
  post("/login") {
  	contentType = formats("json")
  	val loginInfo = parsedBody.extract[LoginInfo]
  	val result = from(UserDb.users)(user => where(user.username === loginInfo.username and user.password === loginInfo.password) select(user))
  	if(result.size == 1) {
 		"{\"error\" : false, \"userInfo\" : {\"userId\" : " + result.head.id + "}}"
  	} else {
		"{\"error\" : true}"
  	}
  }
  
  get("/users") {
  	contentType = formats("json")
  	
	from(UserDb.users)(select(_)).toList
  }
  
}
