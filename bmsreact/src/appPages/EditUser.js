import React, { useState, useEffect} from 'react';
import {
  FormControl,
  FormLabel,
  InputLabel,
  Input,
  Button,
  MenuItem,
  Select
} from "@material-ui/core";

import {restCall} from '../utils/RestComponent'

var editState = {

    };

const EditUser = props => {

    const [apiRet, setApiRet] = useState({});

    const disabled = props.globalData.editUser.id === props.globalData.loginUser.id?(true):(false)

    useEffect(() => {
        if(Object.getOwnPropertyNames(editState).length === 0 || editState.id!==props.globalData.editUser.id){
            editState={...props.globalData.editUser};
        }
        if(apiRet==='Update Successful'){
            editState={...props.globalData.editUser};
        }
    },[apiRet, props.globalData.editUser]);

    function handleChange(event) {
        if(event.target.id==='fname')
            editState.firstName = event.target.value;
        if(event.target.id==='lname')
            editState.lastName = event.target.value;
        if(event.target.id==='phone')
            editState.phone = event.target.value;
        if(event.target.id==='postal')
             editState.postalCode = event.target.value;
        if(event.target.id==='addr1')
             editState.addr1 = event.target.value;
        if(event.target.id==='addr2')
            editState.addr2 = event.target.value;
        if(event.target.id==='answer')
            editState.secretAns = event.target.value;

        if(event.target.id==='oldpass')
            editState.oldPassword = event.target.value;

        if(event.target.id==='password')
            editState.password = event.target.value;

        if(event.target.id==='confirmPassword')
            editState.confirmPassword = event.target.value;

        if(event.target.id==='type')
            editState.type = event.target.value;

        if(event.target.id==='sesnei')
            editState.sesnei = event.target.value;

        editState.cityId = props.globalData.editUser.cityVo.id;
        editState.secretQues = props.globalData.editUser.secretQues;
    };

    function handleTypeChange(event) {
        editState.type = event.target.value;
    }

    const editUser = () =>{
        editState.cityId = props.globalData.editUser.cityVo.id;
        editState.secretQues = props.globalData.editUser.secretQues;
        editState.id = props.globalData.editUser.id;

        restCall('POST', `${props.globalData.serverURI}/api/updateUser`, setApiRet, props.globalData.token, editState);
    };

    return(
        <div
            style={{
            display: "flex",
            justifyContent: "center",
            margin: 20,
            padding: 20
            }}
        >
            <form style={{ width: "80%" }} onChange={handleChange.bind(this)}>
                <h1>Edit User</h1>
                {apiRet.error !== undefined?(<FormLabel component="legend">{apiRet.error}</FormLabel>):(<></>)}
                {apiRet==='Update Successful'?(<FormLabel component="legend">
                    The User should logout and log back in to see your updated changes.
                    </FormLabel>):(<>

                <FormControl margin="normal" fullWidth>
                    <InputLabel htmlFor="emailId">Email Id</InputLabel>
                    <Input id="emailId" type="text" value={props.globalData.editUser.emailId}/>
                </FormControl>

                <FormControl margin="normal" fullWidth>
                    <InputLabel htmlFor="fname">First Name</InputLabel>
                    <Input id="fname" type="text" defaultValue={props.globalData.editUser.firstName}/>
                </FormControl>

                <FormControl margin="normal" fullWidth>
                    <InputLabel htmlFor="lname">Last Name</InputLabel>
                    <Input id="lname" type="text" defaultValue={props.globalData.editUser.lastName}/>
                </FormControl>

                <FormControl margin="normal" fullWidth>
                    <InputLabel htmlFor="phone">Phone (XXX-XXX-XXXX)</InputLabel>
                    <Input id="phone" type="text" defaultValue={props.globalData.editUser.phone}/>
                </FormControl>

                <FormControl margin="normal" fullWidth>
                    <InputLabel htmlFor="add1">Address Line 1</InputLabel>
                    <Input id="addr1" type="text" defaultValue={props.globalData.editUser.addr1}/>
                </FormControl>

                <FormControl margin="normal" fullWidth>
                    <InputLabel htmlFor="add2">Address Line 2</InputLabel>
                    <Input id="addr2" type="text" defaultValue={props.globalData.editUser.addr2}/>
                </FormControl>

                <FormControl margin="normal" fullWidth>
                    <InputLabel htmlFor="postal">Postal Code</InputLabel>
                    <Input id="postal" type="text" defaultValue={props.globalData.editUser.postalCode}/>
                </FormControl>


                {disabled===true?(
                    <FormControl margin="normal" fullWidth>
                        {props.globalData.editUser.type==='U'?(<>User</>):(null)}
                        {props.globalData.editUser.type==='A'?(<div style={{color:'blue'}}>Admin</div>):(null)}
                        {props.globalData.editUser.type==='S'?(<div style={{color:'red'}}>Super User</div>):(null)}
                    </FormControl>):(
                    <>
                        <FormControl margin="normal" fullWidth>
                            <InputLabel htmlFor="type-lable">User type</InputLabel>
                            <Select
                              labelId="type"
                              id="type"
                              defaultValue={props.globalData.editUser.type}
                              onChange={handleTypeChange}
                            >
                              <MenuItem value='U'>User</MenuItem>
                              <MenuItem value='A'>Admin</MenuItem>
                              <MenuItem value='S'>Super User</MenuItem>
                            </Select>
                        </FormControl>
                        <p/>
                    </>
                )}


                <FormControl margin="normal" fullWidth>
                    <InputLabel htmlFor="answer">What is your mothers maiden name</InputLabel>
                    <Input id="answer" type="password" />
                </FormControl>

                <FormControl margin="normal" fullWidth>
                    <InputLabel htmlFor="password">New Password</InputLabel>
                    <Input id="password" type="password" />
                </FormControl>

                <FormControl margin="normal" fullWidth>
                    <InputLabel htmlFor="confirmPassword">Confirm Password</InputLabel>
                    <Input id="confirmPassword" type="password" />
                </FormControl>

                <FormControl margin="normal" fullWidth>
                    <InputLabel htmlFor="oldpass">Existing Password</InputLabel>
                    <Input id="oldpass" type="password" />
                </FormControl>

                <Button variant="contained" color="primary" size="medium" onClick={editUser}>
                    Send
                </Button></>)}
            </form>
        </div>
    );
}

export default EditUser;