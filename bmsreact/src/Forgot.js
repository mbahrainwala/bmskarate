import React, { useState} from 'react';
import {
  FormControl,
  FormLabel,
  InputLabel,
  Input,
  Button
} from "@material-ui/core";
import {restCall} from './utils/RestComponent'

const forgotPassState = {

      };

const Forgot = props => {
    function handleChange(event) {
        if(event.target.id==='email')
            forgotPassState.emailId = event.target.value;

        if(event.target.id==='answer')
            forgotPassState.secretAns = event.target.value;

        forgotPassState.cityId = props.globalData.commonData.cityList[0].id;
        forgotPassState.secretQues = props.globalData.commonData.secQues[0].secQuesCode;
    }

    const [apiRet, setApiRet] = useState({});

    const forgotPass=()=>{
        forgotPassState.cityId = props.globalData.commonData.cityList[0].id;
        forgotPassState.secretQues = props.globalData.commonData.secQues[0].secQuesCode;

        restCall('POST', `${props.globalData.serverURI}/forgot`, setApiRet, '', forgotPassState);
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
            <form style={{ width: "50%" }} onChange={handleChange.bind(this)}>
                <h1>Forgot Password</h1>
                {apiRet.error !== undefined?(<FormLabel component="legend">{apiRet.error}</FormLabel>):(<></>)}
                {apiRet==='reset successful'?(
                    <FormLabel component="legend">A new password has been emailed toy uor account.
                        <p/>Please check your email for further instructions.</FormLabel>
                ):(<>
                <FormControl margin="normal" fullWidth>
                    <InputLabel htmlFor="email">Email</InputLabel>
                    <Input id="email" type="email" />
                </FormControl>

                <FormControl margin="normal" fullWidth>
                    <InputLabel htmlFor="answer">What is your mothers maiden name</InputLabel>
                    <Input id="answer" type="password" />
                </FormControl>

                <Button variant="contained" color="primary" size="medium" onClick={forgotPass}>
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

export default Forgot;