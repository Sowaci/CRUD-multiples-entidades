package com.sena.database_connection.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sena.database_connection.entities.User;
import com.sena.database_connection.repositories.UserRepository;

@Service
public class UserService {

    /*
     * Otra forma de hacerlo:
     * 
     * @Autowired -> Indica que la propiedad siguiente debe ser inyectada
     * private UserRepository repository;
     */

    // Repositorio encargado de las operaciones sobre la tabla users
    private UserRepository repository;

    // Constructor con inyección de dependencias
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    // Método para obtener todos los usuarios registrados
    public List<User> obtenerTodos() {

        // Llama al método findAll de JPARepository para obtener todos los
        // registros de la tabla users.
        return this.repository.findAll();
    }

    // Método para obtener un usuario por id
    public Optional<User> porId(Long id) {

        // Llama al método findById de JPARepository para buscar un usuario
        // utilizando su id.
        //
        // El resultado se encapsula en Optional<User> porque puede ocurrir
        // que no exista un usuario con ese id.
        return this.repository.findById(id);
    }

    // Método para crear un usuario
    // Recibe una instancia de User con el id en null
    public User crear(User user) {

        // Llama al método save de JPARepository para guardar el usuario
        // en la base de datos.
        return this.repository.save(user);
    }

    // Método para actualizar un usuario existente
    // Recibe una instancia de User con el id y los nuevos datos
    public User actualizar(User user) {

        // Se busca el usuario por id para validar que exista
        Optional<User> userFound = this.porId(user.getId());

        // Si el usuario no existe, retorna null
        if (userFound.isEmpty()) {
            return null;
        }

        // Si existe, msave actualizará automáticamente el registro
        // en la base de datos.
        return this.repository.save(user);
    }

    // Método para eliminar un usuario por id
    public User eliminar(Long id) {

        // Se busca el usuario para validar que exista antes de eliminarlo
        Optional<User> userFound = this.porId(id);

        // Si el usuario no existe, retorna null
        if (userFound.isEmpty()) {
            return null;
        }

        // get() obtiene el objeto User contenido dentro del Optional
        // y luego se elimina de la base de datos.
        this.repository.delete(userFound.get());

        // Retorna el usuario eliminado
        return userFound.get();
    }

}