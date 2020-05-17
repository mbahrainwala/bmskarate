import React, { useState, useEffect} from 'react';
import {
  FormControl,
  FormLabel,
  InputLabel,
  Input,
  Button,
  MenuItem,
  Select,
} from "@material-ui/core";

import ListStudents from './ListStudents'
import {restCall} from '../utils/RestComponent'

const EditUser = props => {
    const [apiRet, setApiRet] = useState({});
    const [editState, setEditState] = useState({});

    useEffect(() => {
        if(Object.getOwnPropertyNames(editState).length===0){
            restCall('GET', `${props.globalData.serverURI}/api/getUser?id=${props.globalData.editUser.id}`, setApiRet, props.globalData.token, null);
            if(apiRet!==null && apiRet.errors === undefined && apiRet.id!==undefined){
                const newState = {...apiRet};
                newState.cityId = newState.cityVo.id;
                setEditState(newState);
            }
        }
    },[editState, apiRet, props.globalData.editUser.id, props.globalData.serverURI, props.globalData.token])

    function changeFunc(event){
        const newState = {...editState};
        if(event.target.id==='fname')
            newState.firstName = event.target.value;
        if(event.target.id==='lname')
            newState.lastName = event.target.value;
        if(event.target.id==='phone')
            newState.phone = event.target.value;
        if(event.target.id==='addr1')
            newState.addr1 = event.target.value;
        if(event.target.id==='addr2')
            newState.addr2 = event.target.value;
        if(event.target.id==='postal')
            newState.postalCode = event.target.value;

        if(event.target.id==='answer')
            newState.secretAns = event.target.value;
        if(event.target.id==='password')
            newState.password = event.target.value;
        if(event.target.id==='confirmPassword')
            newState.confirmPassword = event.target.value;
        if(event.target.id==='oldpass')
            newState.oldPassword = event.target.value;

        setEditState(newState);
    }

    function changeUserType(event){
        const newState = {...editState};
        newState.type=event.target.value;
        setEditState(newState);
    }

    const editUser=()=>{
        restCall('POST', `${props.globalData.serverURI}/api/updateUser`, setApiRet, props.globalData.token, editState);
    }

    return(
        <>
        <div
            style={{
            display: "flex",
            justifyContent: "center",
            margin: 20,
            padding: 20
            }}
        >
        {Object.getOwnPropertyNames(editState).length !== 0 ?(
            <form style={{ width: "80%" }}>
                <h1>{editState.id===props.globalData.loginUser.id?(<>Your Profile</>):(<>Edit User</>)}</h1>
                {apiRet!==null && apiRet.error !== undefined?(<FormLabel component="legend">{apiRet.error}</FormLabel>):(<></>)}
                {apiRet!==null && apiRet === 'Update Successful'?(<FormLabel component="legend">Update Successful</FormLabel>):(<></>)}
                <InputLabel htmlFor="emailId">Email Id</InputLabel>
                <FormControl margin="normal" fullWidth>
                    <Input id="emailId" type="text" value={editState.emailId}/>
                </FormControl>


                <InputLabel htmlFor="fname">First Name</InputLabel>
                <FormControl margin="normal" fullWidth>
                    <Input id="fname" type="text" value={editState.firstName} onChange={changeFunc}/>
                </FormControl>

                <InputLabel htmlFor="lname">Last Name</InputLabel>
                <FormControl margin="normal" fullWidth>
                    <Input id="lname" type="text" value={editState.lastName} onChange={changeFunc}/>
                </FormControl>

                <InputLabel htmlFor="phone">Phone (XXX-XXX-XXXX)</InputLabel>
                <FormControl margin="normal" fullWidth>
                    <Input id="phone" type="text" value={editState.phone} onChange={changeFunc}/>
                </FormControl>

                <InputLabel htmlFor="add1">Address Line 1</InputLabel>
                <FormControl margin="normal" fullWidth>
                    <Input id="addr1" type="text" value={editState.addr1} onChange={changeFunc}/>
                </FormControl>

                <InputLabel htmlFor="add2">Address Line 2</InputLabel>
                <FormControl margin="normal" fullWidth>
                    <Input id="addr2" type="text" value={editState.addr2} onChange={changeFunc}/>
                </FormControl>

                <InputLabel htmlFor="postal">Postal Code</InputLabel>
                <FormControl margin="normal" fullWidth>
                    <Input id="postal" type="text" value={editState.postalCode} onChange={changeFunc}/>
                </FormControl>

                {editState.id===props.globalData.loginUser.id?(<>
                <FormControl margin="normal" fullWidth>
                    {props.globalData.editUser.type==='U'?(<>User</>):(null)}
                    {props.globalData.editUser.type==='A'?(<div style={{color:'blue'}}>Admin</div>):(null)}
                    {props.globalData.editUser.type==='S'?(<div style={{color:'red'}}>Super User</div>):(null)}
                </FormControl></>):(
                <>
                <InputLabel htmlFor="type-lable">User type</InputLabel>
                <FormControl margin="normal" fullWidth>
                    <Select
                      labelId="type"
                      id="type"
                      value={editState.type}
                      onChange={changeUserType}
                    >
                      <MenuItem value='U'>User</MenuItem>
                      <MenuItem value='A'>Admin</MenuItem>
                      <MenuItem value='S'>Super User</MenuItem>
                    </Select>
                </FormControl>
                </>)}

                <FormControl margin="normal" fullWidth>
                    <InputLabel htmlFor="answer">Mothers maiden name</InputLabel>
                    <Input id="answer" type="password" onChange={changeFunc}/>
                </FormControl>

                <FormControl margin="normal" fullWidth>
                    <InputLabel htmlFor="password">New Password</InputLabel>
                    <Input id="password" type="password" onChange={changeFunc}/>
                </FormControl>

                <FormControl margin="normal" fullWidth>
                    <InputLabel htmlFor="confirmPassword">Confirm Password</InputLabel>
                    <Input id="confirmPassword" type="password" onChange={changeFunc}/>
                </FormControl>

                {editState.id!==props.globalData.loginUser.id?(null):(
                    <FormControl margin="normal" fullWidth>
                        <InputLabel htmlFor="oldpass">Existing Password</InputLabel>
                        <Input id="oldpass" type="password" onChange={changeFunc}/>
                    </FormControl>)}

                <Button variant="contained" color="primary" size="medium" onClick={editUser}>
                    Send
                </Button>
            </form>):null}
        </div>
        {editState.id!==props.globalData.loginUser.id?(
        <ListStudents globalData={props.globalData}
            setGlobalData={props.setGlobalData}
            token={props.globalData.token}
            serverURI={props.globalData.serverURI} listFor='remove'/>):
        (
                <ListStudents globalData={props.globalData}
                    setGlobalData={props.setGlobalData}
                    token={props.globalData.token}
                    serverURI={props.globalData.serverURI} listFor='list'/>)
        }
        {editState.id!==props.globalData.loginUser.id?(
            <ListStudents globalData={props.globalData}
                setGlobalData={props.setGlobalData}
                token={props.globalData.token}
                serverURI={props.globalData.serverURI} listFor='add'/>):null}
        </>
    );
}

export default EditUser;