import React, {useState}  from "react";
import {
  FormControl,
  InputLabel,
  Input,
  Button
} from "@material-ui/core";
import {restCall} from './utils/RestComponent'

const loginState = {};

const Login = props => {
    function handleChange(event) {
        if(event.target.id==='email')
            loginState.emailId=event.target.value;
        if(event.target.id==='password')
            loginState.password=event.target.value;
    }

    const [apiRet, setApiRet] = useState({});

    const loginUser = (method, uri) => {
        restCall('POST', `${props.globalData.serverURI}/login`, loginState, setApiRet);

        if(apiRet.error !== undefined){
        }
        else if(apiRet.token !== undefined){
            //put the token and user into the global vars.
            const newProps = {...props.globalData};
            newProps.token = apiRet.token;
            newProps.loggedUser = apiRet.user;
            newProps.page = 'mainApp';
            props.setGlobalData(newProps);
        }

    }

    return(
        <div
            style={{
            display: "flex",
            justifyContent: "center",
            margin: 20,
            padding: 20
            }}
        >

        <form style={{ width: "50%" }} onChange={handleChange}>
            <h1>Login</h1>

            <FormControl margin="normal" fullWidth>
                <InputLabel htmlFor="email">Email</InputLabel>
                <Input id="email" type="email" />
            </FormControl>

            <FormControl margin="normal" fullWidth>
                <InputLabel htmlFor="password">Password</InputLabel>
                <Input id="password" type="password" />
            </FormControl>

            <Button variant="contained" color="primary" size="medium" onClick={loginUser}>
                Login
            </Button>

            <Button color="primary" size="medium" onClick={()=>{
                    const newProps = {...props.globalData};
                    newProps.page='register';
                    props.setGlobalData(newProps);
                }}>
                Register
            </Button>

            <Button color="primary" size="medium" onClick={()=>{
                    const newProps = {...props.globalData};
                    newProps.page='forgot';
                    props.setGlobalData(newProps);
                }}>
                Forgot Password
            </Button>
        </form>

        </div>
    );
}

export default Login;