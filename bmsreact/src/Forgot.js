import React, { useState} from 'react';
import {
  FormControl,
  FormLabel,
  InputLabel,
  Input,
  Button
} from "@material-ui/core";
import {restCall} from './utils/RestComponent'

const Forgot = props => {
    function handleChange(event) {

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
            <form style={{ width: "50%" }} onChange={handleChange.bind(this)} id="registrationForm">
                <h1>Forgot Password</h1>
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