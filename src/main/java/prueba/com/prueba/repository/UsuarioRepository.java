package prueba.com.prueba.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import prueba.com.prueba.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
