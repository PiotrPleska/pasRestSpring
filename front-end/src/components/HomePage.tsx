import * as yup from 'yup';
import {useForm} from "react-hook-form";
import {yupResolver} from "@hookform/resolvers/yup";
import {api} from "../api/api.ts";
import {AccountLogin} from "../types/Account.ts";
import {jwtDecode, JwtPayload} from "jwt-decode";
import {useUserContext} from "../Context/UserProvider.tsx";
import {AccountTypeEnum} from "../enums/AccountType.enum.ts";
import {useNavigate} from "react-router-dom";

interface CustomJwtPayload extends JwtPayload {
    role?: string;
}

const schema = yup.object({
    login: yup.string().required('Last Name is required').min(2, 'Must be at least 2 characters').max(20, 'Must be less than 20 characters'),
    password: yup.string().required('Password is required').min(6, 'Must be at least 2 characters').max(20, 'Must be less than 20 characters'),
})

type AddUserFormType = yup.InferType<typeof schema>;
export function HomePage() {
    const {
        register,
        handleSubmit,
        formState: {errors},
    } = useForm<AddUserFormType>({
            resolver: yupResolver(schema),
        }
    );
    const {setUser} = useUserContext();
    const naviagte = useNavigate();

    const handleConfirmSubmit = async (formData: AddUserFormType) => {
        const client: AccountLogin = {
            login: formData?.login,
            password: formData?.password
        }
        console.log(client.login);
        console.log(client.password);
        const token = await api.logIn(client);
        const etag = token.headers.etag;
        localStorage.setItem('etag', etag);
        console.log("etag: " + etag);
        const decodedToken = jwtDecode<CustomJwtPayload>(token.data);

        localStorage.setItem('token', token.data);
        console.log("item set to :" + localStorage.getItem('token'));
        let userRole: AccountTypeEnum;
        console.log(decodedToken?.role);
        if (decodedToken?.role === 'ROLE_ADMIN') {
            userRole = AccountTypeEnum.ADMIN
        } else if (decodedToken?.role === 'ROLE_CLIENT') {
            userRole = AccountTypeEnum.CLIENT
        } else {
            userRole = AccountTypeEnum.RESOURCE_MANAGER
        }
        setUser({
            login: client.login? client.login : '',
            accountType: userRole,
        });
        if (userRole === AccountTypeEnum.CLIENT) {
            naviagte('/Home');
        }
    };

    const handleSubmitFrom = (data: AddUserFormType) => {
        handleConfirmSubmit(data);
    }

    return (
        <>
            <h1>Log in</h1>
            <form className="form-label" onSubmit={handleSubmit((data) => handleSubmitFrom(data))}>
                <label htmlFor="login">Login</label>
                <input id="login" type="text" {...register('login', { required: true })} />
                <br/>
                {errors.login && <span style={{ color: 'red' }}>{errors.login.message}</span>}
                <br />
                <label htmlFor="password">Password</label>
                <input id="password" type="password" {...register('password', { required: true })} />
                <br/>
                {errors.password && <span style={{ color: 'red' }}>{errors.password.message}</span>}
                <br />
                <button type="submit">Log In</button>
                <br />
                <button onClick={() => naviagte('/register')}>Register</button>
            </form>
        </>
    );
}