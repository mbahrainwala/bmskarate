import React, { useState} from 'react';
import {
  FormControl,
  FormLabel,
  InputLabel,
  Input,
  Button
} from "@material-ui/core";

import {restCall} from './utils/RestComponent'

const registrationState = {

      };

const Register = props => {
    function handleChange(event) {
        if(event.target.id==='email')
            registrationState.emailId = event.target.value;
        if(event.target.id==='fname')
            registrationState.firstName = event.target.value;
        if(event.target.id==='lname')
            registrationState.lastName = event.target.value;
        if(event.target.id==='phone' && /^\d+$/.test(event.target.value))
            registrationState.phone = event.target.value;
        if(event.target.id==='postal')
             registrationState.postalCode = event.target.value;
        if(event.target.id==='addr1')
             registrationState.addr1 = event.target.value;
        if(event.target.id==='addr2')
            registrationState.addr2 = event.target.value;
        if(event.target.id==='answer')
            registrationState.secretAns = event.target.value;

        registrationState.type='U';
        registrationState.cityId = props.globalData.commonData.cityList[0].id;
        registrationState.secretQues = props.globalData.commonData.secQues[0].secQuesCode;
    }

    const [apiRet, setApiRet] = useState({});

    const registerUser=()=>{
        registrationState.cityId = props.globalData.commonData.cityList[0].id;
        registrationState.secretQues = props.globalData.commonData.secQues[0].secQuesCode;

        restCall('POST', `${props.globalData.serverURI}/register`, setApiRet, '', registrationState);
    }

    return (
        <div
            style={{
              display: "flex",
              justifyContent: "center",
              margin: 20,
              padding: 20
            }}
          >
          <form style={{ width: "50%" }} onChange={handleChange.bind(this)}>
            <h1>Register</h1>
            {apiRet.error !== undefined?(<FormLabel component="legend">{apiRet.error}</FormLabel>):(<></>)}
            {apiRet==='registration successful'?(<FormLabel component="legend">
                Registration is successful. Please check your email for further instructions.<p/>
            </FormLabel>):(<>

            <FormControl margin="normal" fullWidth>
                <InputLabel htmlFor="email">Email</InputLabel>
                <Input id="email" type="email" />
            </FormControl>

            <FormControl margin="normal" fullWidth>
                <InputLabel htmlFor="fname">First Name</InputLabel>
                <Input id="fname" type="text" />
            </FormControl>

            <FormControl margin="normal" fullWidth>
                <InputLabel htmlFor="lname">Last Name</InputLabel>
                <Input id="lname" type="text" />
            </FormControl>

            <FormControl margin="normal" fullWidth>
                <InputLabel htmlFor="phone">Phone (Only numbers will be accepted.)</InputLabel>
                <Input id="phone" type="text" />
            </FormControl>

            <FormControl margin="normal" fullWidth>
                <InputLabel htmlFor="add1">Address Line 1</InputLabel>
                <Input id="addr1" type="text" />
            </FormControl>

            <FormControl margin="normal" fullWidth>
                <InputLabel htmlFor="add2">Address Line 2</InputLabel>
                <Input id="addr2" type="text" />
            </FormControl>

            <FormControl margin="normal" fullWidth>
                <InputLabel htmlFor="postal">Postal Code</InputLabel>
                <Input id="postal" type="text" />
            </FormControl>

            <FormControl margin="normal" fullWidth>
                <InputLabel htmlFor="answer">What is your mothers maiden name</InputLabel>
                <Input id="answer" type="password" />
            </FormControl>

            <Button variant="contained" color="primary" size="medium" onClick={registerUser}>
                Send
            </Button></>)}

            <Button color="primary" size="medium" onClick={()=>{
                const newProps = {...props.globalData};
                newProps.page='login';
                props.setGlobalData(newProps);
                }}>
                Login
            </Button>
          </form>
        </div>
    );
}

export default Register;