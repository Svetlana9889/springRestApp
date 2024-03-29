$(async function () {
    await allUsers();
    await newUser();
    deleteUser();
    editCurrentUser();
});

async function allUsers() {
    const table = $('#bodyAllUserTable');
    table.empty()
    fetch("http://localhost:8080/api/admin")
        .then(r => r.json())
        .then(data => {
            data.forEach(userDto => {
                let users = `$(
                        <tr>
<!--                            <td>${user.id}</td>-->
                            <td>${userDto.name}</td>
                            <td>${userDto.surname}</td>
                            <td>${userDto.email}</td>
                            <td>${userDto.roles.map(role => " " + role.name.substring(5))}</td>
                            <td>
                                <button type="button" class="btn btn-info" data-toggle="modal" id="buttonEdit" data-action="edit" data-id="${userDto.username}" data-target="#edit">Edit</button>
                            </td>
                            <td>
                                <button type="button" class="btn btn-danger" data-toggle="modal" id="buttonDelete" data-action="delete" data-id="${userDto.username}" data-target="#delete">Delete</button>
                            </td>
                        </tr>)`;
                table.append(users);
            })
        })
        .catch((error) => {
            alert(error);
        })
}

async function newUser() {
    await fetch("http://localhost:8080/api/admin/roles")
        .then(r => r.json())
        .then(roles => {
            roles.forEach(role => {
                let element = document.createElement("option");
                element.text = role.name.substring(5);
                element.value = role.id;
                $('#rolesNewUser')[0].appendChild(element);
            })
        })

   const formAddNewUser = document.forms["formAddNewUser"];

    formAddNewUser.addEventListener('submit', function (event) {
        event.preventDefault();
        let rolesNewUser = [];
        for (let i = 0; i < formAddNewUser.roles.options.length; i++) {
            if (formAddNewUser.roles.options[i].selected) rolesNewUser.push({
                id: formAddNewUser.roles.options[i].value,
                name: formAddNewUser.roles.options[i].name
            })
        }

        fetch("http://localhost:8080/api/admin/addUser", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                name: formAddNewUser.name.value,
                surname: formAddNewUser.surname.value,
                username: formAddNewUser.username.value,
                email: formAddNewUser.email.value,
                password: formAddNewUser.password.value,
                roles: rolesNewUser
            })
        }).then(() => {
            formAddNewUser.reset();
            allUsers();
            $('#allUsersTable').click();
        })
            .catch((error) => {
                alert(error);
            })
    })

}

function deleteUser() {
    const deleteForm = document.forms["formDeleteUser"];
    deleteForm.addEventListener("submit", function (event) {
        event.preventDefault();
        fetch("http://localhost:8080/api/admin/removeUser/" + deleteForm.username.value, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(() => {
                $('#deleteFormCloseButton').click();
                allUsers();
            })
            .catch((error) => {
                alert(error);
            });
    })
}


$(document).ready(function () {
    $('#delete').on("show.bs.modal", function (event) {
        const button = $(event.relatedTarget);
        const username = button.data("username");
        console.log("Username перед вызовом функции getUser:", username);
        viewDeleteModal(username);
    })
})

async function viewDeleteModal(username) {
    let userDelete = await getUser(username);
    let formDelete = document.forms["formDeleteUser"];
    // formDelete.id.value = userDelete.id;
    formDelete.name.value = userDelete.name;
    formDelete.surname.value = userDelete.surname;
    formDelete.email.value = userDelete.email;

    $('#deleteRolesUser').empty();

    await fetch("http://localhost:8080/api/admin/roles")
        .then(r => r.json())
        .then(roles => {
            roles.forEach(role => {
                let selectedRole = false;
                for (let i = 0; i < userDelete.roles.length; i++) {
                    if (userDelete.roles[i].name === role.name) {
                        selectedRole = true;
                        break;
                    }
                }
                let element = document.createElement("option");
                element.text = role.name.substring(5);
                element.value = role.id;
                if (selectedRole) element.selected = true;
                $('#deleteRolesUser')[0].appendChild(element);
            })
        })
        .catch((error) => {
            alert(error);
        })
}

async function getUser(username) {
    if (!username) {
        // Handle the case where username is not provided
        console.error("Username is missing");
        return null;
    }

    let url = "http://localhost:8080/api/admin/" + username;
    let response = await fetch(url);
    return await response.json();
}

function editCurrentUser() {
    const editForm = document.forms["formEditUser"];
    editForm.addEventListener("submit", function (event) {
        event.preventDefault();
        let editUserRoles = [];
        for (let i = 0; i < editForm.roles.options.length; i++) {
            if (editForm.roles.options[i].selected) editUserRoles.push({
                id: editForm.roles.options[i].value,
                name: editForm.roles.options[i].name
            })
        }

        fetch("http://localhost:8080/api/admin/addOrUpdate/" + editForm.username.value, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                // id: editForm.id.value,
                name: editForm.name.value,
                surname: editForm.surname.value,
                email: editForm.email.value,
                password: editForm.password.value,
                roles: editUserRoles
            })
        }).then(() => {
            $('#editFormCloseButton').click();
            allUsers();
        })
            .catch((error) => {
                alert(error);
            })
    })
}

$(document).ready(function () {
    $('#edit').on("show.bs.modal", function (event) {
        const button = $(event.relatedTarget);
        const username = button.data("username");
        viewEditModal(username);
    })
})

async function viewEditModal(username) {
    let userEdit = await getUser(username);
    let form = document.forms["formEditUser"];
    // form.id.value = userEdit.id;
    form.name.value = userEdit.name;
    form.surname.value = userEdit.surname;
    form.email.value = userEdit.email;
    form.password.value = userEdit.password;

    $('#editRolesUser').empty();

    await fetch("http://localhost:8080/api/admin/roles")
        .then(r => r.json())
        .then(roles => {
            roles.forEach(role => {
                let selectedRole = false;
                for (let i = 0; i < userEdit.roles.length; i++) {
                    if (userEdit.roles[i].name === role.name) {
                        selectedRole = true;
                        break;
                    }
                }
                let element = document.createElement("option");
                element.text = role.name.substring(5);
                element.value = role.id;
                if (selectedRole) element.selected = true;
                $('#editRolesUser')[0].appendChild(element);
            })
        })
        .catch((error) => {
            alert(error);
        })
}